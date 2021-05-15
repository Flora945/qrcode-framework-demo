package com.jlpay.qrcode.external.business.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlpay.qrcode.external.business.services.DelayCancelService;
import com.jlpay.qrcode.external.business.services.TimedQueryService;
import com.jlpay.qrcode.external.business.services.protocol.DelayCancelMessage;
import com.jlpay.qrcode.external.business.services.protocol.request.ChannelBarCodeRequest;
import com.jlpay.qrcode.external.business.services.protocol.request.ChannelBaseRequest;
import com.jlpay.qrcode.external.business.services.protocol.response.ChannelBaseResponse;
import com.jlpay.qrcode.external.business.process.base.AbstractExtQrcodeApiBusiProcess;
import com.jlpay.qrcode.external.business.protocol.enums.PayType;
import com.jlpay.qrcode.external.business.protocol.request.MicropayRequest;
import com.jlpay.qrcode.external.business.protocol.response.MicropayResponse;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionMessages;
import com.jlpay.qrcode.external.commons.io.client.ReactiveHttpTool;
import com.jlpay.qrcode.external.config.properties.QrcodeApiProperties;
import com.jlpay.qrcode.external.config.properties.QrcodeEngineProperties;
import com.jlpay.qrcode.external.db.model.OrderStatusType;
import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import com.jlpay.qrcode.external.db.model.QrPromotionInfo;
import com.jlpay.qrcode.external.db.service.IOutQrPayOrderService;
import com.jlpay.qrcode.external.db.service.QrPromotionInfoService;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.qrcode.external.support.QrcodeApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * B2C扫码(被扫/条码)交易
 *
 * @author qihuaiyuan
 * @since 2021/5/5
 */
@Slf4j
@RequiredArgsConstructor
@Component(ApiConstants.MICROPAYASYN_SERVICE)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BarCodePayProcess extends AbstractExtQrcodeApiBusiProcess<MicropayRequest> {

    private final IOutQrPayOrderService outQrPayOrderService;

    private final QrcodeEngineProperties qrcodeEngineProperties;

    private final QrcodeApiProperties qrcodeApiConfig;

    private final QrPromotionInfoService qrPromotionInfoService;

    private final TimedQueryService timedQueryService;
    
    private final DelayCancelService delayCancelService;

    private final ReactiveHttpTool httpTool;

    private OutQrPayOrder outQrPayOrder;

    private ChannelBaseResponse chnResponse;

    @Override
    protected void handleExtQrcodeBusiness() {
        MicropayRequest micropayRequest = getRequest();
        //保存订单
        outQrPayOrder = outQrPayOrderService.createOrder(micropayRequest);
        //保存优惠信息
        if (StringUtils.isNotBlank(micropayRequest.getGoodsTag()) || StringUtils.isNotBlank(micropayRequest.getGoodsData())) {
            qrPromotionInfoService.savePromotionInfo(micropayRequest, outQrPayOrder);
        }

        // 调用码付交易系统发起交易
        invokeFor(ChannelBaseResponse.class)
                .servicePath(qrcodeEngineProperties.getUrl() + ApiConstants.CHN_BARCODE)
                .requestBody(createQrcodeTransRequest())
                .onResponse(this::handleQrcodeTransResponse)
                .invoke();
    }

    /**
     * 创建码付交易请求
     *
     * @return
     */
    private ChannelBarCodeRequest createQrcodeTransRequest() {
        ChannelBarCodeRequest channelBarCodeRequest = new ChannelBarCodeRequest();
        MicropayRequest micropayRequest = getRequest();
        channelBarCodeRequest.setCommandId(ApiConstants.CHN_BARCODE);
        channelBarCodeRequest.setOrgCode(outQrPayOrder.getOrgCode());

        channelBarCodeRequest.setPayType(micropayRequest.getPayType());
        channelBarCodeRequest.setOrderId(outQrPayOrder.getOrderId());
        channelBarCodeRequest.setAmount(micropayRequest.getTotalFee());
        channelBarCodeRequest.setMerchNo(outQrPayOrder.getMerchNo());
        channelBarCodeRequest.setTermNo(micropayRequest.getTermNoOrDeviceInfo());
        channelBarCodeRequest.setAuthCode(micropayRequest.getAuthCode());
        channelBarCodeRequest.setBusiType(outQrPayOrder.getBusiType());
        channelBarCodeRequest.setBusiSubType(outQrPayOrder.getBusiSubType());
        channelBarCodeRequest.setSubject(micropayRequest.getBody());
        channelBarCodeRequest.setSubjectDetail(micropayRequest.getAttach());
        channelBarCodeRequest.setRemark(micropayRequest.getRemark());
        channelBarCodeRequest.setTransTime(outQrPayOrder.getTransTime());
        channelBarCodeRequest.setTransIp(micropayRequest.getMchCreateIp());
        //若接入方未上送订单超时时间,则默认送20分钟
        channelBarCodeRequest.setPaymentValidTime(StringUtils.isNotEmpty(micropayRequest.getPaymentValidTime()) ? micropayRequest.getPaymentValidTime() : qrcodeApiConfig.getPayValidTime());
        channelBarCodeRequest.setLimitPay(micropayRequest.getLimitPay());
        /**设置花呗分期参数**/
        channelBarCodeRequest.setIsHirePurchase(micropayRequest.getIsHirePurchase());
        channelBarCodeRequest.setHirePurchaseNum(micropayRequest.getHirePurchaseNum());
        channelBarCodeRequest.setHirePurchaseSellerPercent(micropayRequest.getHirePurchaseSellerPercent());

        channelBarCodeRequest.setLatitude(micropayRequest.getLatitude());
        channelBarCodeRequest.setLongitude(micropayRequest.getLongitude());
        channelBarCodeRequest.setOpShopId(micropayRequest.getOpShopId());
        channelBarCodeRequest.setOpUserId(micropayRequest.getOpUserId());

        channelBarCodeRequest.setOpDeviceId(micropayRequest.getDeviceInfo());
        channelBarCodeRequest.setSubAppid(micropayRequest.getSubAppid());

        channelBarCodeRequest.setLMerchInfo(lMerchInfo);

        channelBarCodeRequest.setGoodsTag(micropayRequest.getGoodsTag());
        JSONObject chnReqParams = new JSONObject();
        if (ApiConstants.UNIONPAY.equals(micropayRequest.getPayType())) {
            if (StringUtils.isNotBlank(micropayRequest.getGoodsData())) {
                chnReqParams.put(ApiConstants.UNIONPAY_ACQADDNDATA, micropayRequest.getGoodsData());
            }

        } else if (ApiConstants.WXPAY.equals(micropayRequest.getPayType())) {
            if (StringUtils.isNotBlank(micropayRequest.getGoodsData())) {
                chnReqParams.put(ApiConstants.WXPAY_DETAIL, micropayRequest.getGoodsData());
            }
        }

        if (!chnReqParams.isEmpty()) {
            //有需透传的渠道请求字段,才上送
            channelBarCodeRequest.setChannelRequestParams(chnReqParams);
        }
        return channelBarCodeRequest;
    }

    /**
     * 码付交易核心交易状态返回：
     * 交易状态  0-初始订单；1-待确认订单，s-交易成功，f-交易失败，c-交易已撤销，r-交易已退款
     */
    private void handleQrcodeTransResponse(ChannelBaseResponse chnResponse) {
        this.chnResponse = chnResponse;
        OrderStatusType status;
        if (StringUtils.isNotEmpty(chnResponse.getStatus())) {
            status = waitFinalPayResult(OrderStatusType.getStatusType(chnResponse.getStatus()));
        } else {
            status = OrderStatusType.FAIL;
        }
        updateOrder(status.getOutStatus());
        // 非终态添加延迟查询
        if (!OrderStatusType.isFinalStatus(status)) {
            timedQueryService.addTimedQueryTask(outQrPayOrder.getOrderId());
        }
        completeResponse(createResponse());
    }

    /***
     * 是否等待最终结果
     * @param orderStatusType
     */
    private OrderStatusType waitFinalPayResult(OrderStatusType orderStatusType) {
        if (OrderStatusType.WAIT.equals(orderStatusType) && getRequest().isSyncAble()) {
            log.info("同步被扫,待确认,需查询最终结果");
            int count = 1;
            ChannelBaseResponse chnResponse = new ChannelBaseResponse();
            while (count <= qrcodeEngineProperties.getQueryTimes()) {
                log.info("第{}次,发起查询", count);
                count += 1;
                try {
                    log.info("开始休眠:{}ms", qrcodeEngineProperties.getQueryWaitTime());
                    Thread.sleep(qrcodeEngineProperties.getQueryWaitTime());
                } catch (InterruptedException e) {
                    log.error("休眠等待异常", e);
                }
                ChannelBaseRequest channelBaseRequest = new ChannelBaseRequest();
                channelBaseRequest.setCommandId(ApiConstants.CHN_ORDER_QUERY);
                channelBaseRequest.setOrderId(outQrPayOrder.getOrderId());

                String response = httpTool.preparePost()
                        .url(qrcodeEngineProperties.getUrl() + ApiConstants.CHN_ORDER_QUERY)
                        .requestBody(channelBaseRequest)
//                        .requestTimeout()
                        .execute()
                        .block();

                chnResponse = JSON.parseObject(response, ChannelBaseResponse.class);
                if (OrderStatusType.SUCCESS.getEnginStatus().equals(chnResponse.getStatus())) {
                    //查询成功,则重新赋值,渠道应答对象
                    orderStatusType = OrderStatusType.SUCCESS;
                    log.info("第{}次,查询订单:交易成功", count - 1);
                    break;
                }
                log.info("第{}次,查询订单状态未成功", count - 1);
            }

            if (!OrderStatusType.SUCCESS.equals(orderStatusType)) {
                log.info("达到最大查询次数,仍未成功,更改订单状态为失败,并发起撤销");
                orderStatusType = OrderStatusType.FAIL;
                chnResponse.setRetCode(ExceptionCodes.NETWORK_EXCEPTION);
                chnResponse.setRetMsg(ExceptionMessages.TRANSACTION_TIMEOUT_RETRY);
                chnResponse.setChnRetMsg("交易超时,请重试.若已付款,将退回付款账户-JL");

                doCancel(outQrPayOrder);
            }

            completeResponse(chnResponse);
        }
        return orderStatusType;
    }

    private void doCancel(OutQrPayOrder outQrPayOrder) {
        log.info("内部撤销任务,放入延迟队列");
        DelayCancelMessage DelayCancelMessage = new DelayCancelMessage(qrcodeEngineProperties.getDelayCancelTime());
        DelayCancelMessage.setOutQrPayOrder(outQrPayOrder);
        delayCancelService.addToDelayQueue(DelayCancelMessage);
    }

    /**
     * 订单状态   0-新建订单，1-等待确认，2-成功，3-失败,4-已撤销/已冲正，5-已退款
     *
     * @param status
     */
    private void updateOrder(String status) {
        log.debug("被扫:更新订单状态为 {}", OrderStatusType.getOutStautsType(status).name());
        outQrPayOrder.setStatus(status);
        outQrPayOrder.setRetCode(chnResponse.getRetCode());
        outQrPayOrder.setRetMsg(StringUtils.isNotEmpty(chnResponse.getChnRetMsg()) ? chnResponse.getChnRetMsg() : chnResponse.getRetMsg());
        outQrPayOrder.setChnOrderNo(chnResponse.getChnOrderId());

        outQrPayOrder.setOpenid(chnResponse.getSubOpenId());

        outQrPayOrder.setSettleType(chnResponse.getDiscountSettleType());
        outQrPayOrder.setFinnalAmount(QrcodeApiUtils.parseStrToLong(chnResponse.getFinnalAmount()));
        outQrPayOrder.setDiscountAmount(QrcodeApiUtils.parseStrToLong(chnResponse.getDiscountAmount()));
        outQrPayOrder.setSettleAmount(QrcodeApiUtils.parseStrToLong(chnResponse.getSettlementAmt()));
        outQrPayOrder.setDiscountName(chnResponse.getDiscountName());
        outQrPayOrder.setCouponInfo(chnResponse.getCouponInfo());

        outQrPayOrderService.update(outQrPayOrder);

        if (OrderStatusType.SUCCESS.getOutStatus().equals(status) && StringUtils.isNotBlank(outQrPayOrder.getCouponInfo())) {
            QrPromotionInfo qrPromotionInfo = qrPromotionInfoService.queryByOrderId(outQrPayOrder.getOrderId());
            if (qrPromotionInfo != null) {
                qrPromotionInfo.setCouponInfo(outQrPayOrder.getCouponInfo());
                qrPromotionInfo.setDctGoodsInfo(chnResponse.getDctGoodsInfo());
                qrPromotionInfoService.update(qrPromotionInfo);
            }
        }
    }

    private MicropayResponse createResponse() {
        log.debug("被扫,组织应答报文返回");
        MicropayResponse micropayResponse = new MicropayResponse();
        micropayResponse.setStatus(outQrPayOrder.getStatus());
        micropayResponse.setOutTradeNo(outQrPayOrder.getOutOrderId());
        micropayResponse.setChnTransactionId(outQrPayOrder.getChnOrderNo());
        micropayResponse.setTermNo(outQrPayOrder.getTermNo());
        micropayResponse.setDeviceInfo(outQrPayOrder.getOutDeviceSn());
        micropayResponse.setMchId(outQrPayOrder.getMerchNo());
        micropayResponse.setOrderTime(outQrPayOrder.getAddTime());
        micropayResponse.setTransTime(outQrPayOrder.getTransTime());
        micropayResponse.setTotalFee(outQrPayOrder.getAmount().toString());
        micropayResponse.setTransactionId(outQrPayOrder.getOrderId());
        micropayResponse.setRetCode(outQrPayOrder.getRetCode());
        micropayResponse.setRetMsg(outQrPayOrder.getRetMsg());

        micropayResponse.setFinnalAmount(QrcodeApiUtils.parseLongToStr(outQrPayOrder.getFinnalAmount()));
        micropayResponse.setDiscountAmount(QrcodeApiUtils.parseLongToStr(outQrPayOrder.getDiscountAmount()));
        micropayResponse.setDiscountName(outQrPayOrder.getDiscountName());
        micropayResponse.setCouponInfo(outQrPayOrder.getCouponInfo());
        micropayResponse.setSubOpenid(outQrPayOrder.getOpenid());
        micropayResponse.setPayType(PayType.findPayTypeByFeeC(outQrPayOrder.getFeeCalcType()).getPayType());

        micropayResponse.setDctGoodsInfo(chnResponse.getDctGoodsInfo());
        return micropayResponse;
    }
}
