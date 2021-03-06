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
 * B2C??????(??????/??????)??????
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
        //????????????
        outQrPayOrder = outQrPayOrderService.createOrder(micropayRequest);
        //??????????????????
        if (StringUtils.isNotBlank(micropayRequest.getGoodsTag()) || StringUtils.isNotBlank(micropayRequest.getGoodsData())) {
            qrPromotionInfoService.savePromotionInfo(micropayRequest, outQrPayOrder);
        }

        // ????????????????????????????????????
        invokeFor(ChannelBaseResponse.class)
                .servicePath(qrcodeEngineProperties.getUrl() + ApiConstants.CHN_BARCODE)
                .requestBody(createQrcodeTransRequest())
                .onResponse(this::handleQrcodeTransResponse)
                .invoke();
    }

    /**
     * ????????????????????????
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
        //???????????????????????????????????????,????????????20??????
        channelBarCodeRequest.setPaymentValidTime(StringUtils.isNotEmpty(micropayRequest.getPaymentValidTime()) ? micropayRequest.getPaymentValidTime() : qrcodeApiConfig.getPayValidTime());
        channelBarCodeRequest.setLimitPay(micropayRequest.getLimitPay());
        /**????????????????????????**/
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
            //?????????????????????????????????,?????????
            channelBarCodeRequest.setChannelRequestParams(chnReqParams);
        }
        return channelBarCodeRequest;
    }

    /**
     * ???????????????????????????????????????
     * ????????????  0-???????????????1-??????????????????s-???????????????f-???????????????c-??????????????????r-???????????????
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
        // ???????????????????????????
        if (!OrderStatusType.isFinalStatus(status)) {
            timedQueryService.addTimedQueryTask(outQrPayOrder.getOrderId());
        }
        completeResponse(createResponse());
    }

    /***
     * ????????????????????????
     * @param orderStatusType
     */
    private OrderStatusType waitFinalPayResult(OrderStatusType orderStatusType) {
        if (OrderStatusType.WAIT.equals(orderStatusType) && getRequest().isSyncAble()) {
            log.info("????????????,?????????,?????????????????????");
            int count = 1;
            ChannelBaseResponse chnResponse = new ChannelBaseResponse();
            while (count <= qrcodeEngineProperties.getQueryTimes()) {
                log.info("???{}???,????????????", count);
                count += 1;
                try {
                    log.info("????????????:{}ms", qrcodeEngineProperties.getQueryWaitTime());
                    Thread.sleep(qrcodeEngineProperties.getQueryWaitTime());
                } catch (InterruptedException e) {
                    log.error("??????????????????", e);
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
                    //????????????,???????????????,??????????????????
                    orderStatusType = OrderStatusType.SUCCESS;
                    log.info("???{}???,????????????:????????????", count - 1);
                    break;
                }
                log.info("???{}???,???????????????????????????", count - 1);
            }

            if (!OrderStatusType.SUCCESS.equals(orderStatusType)) {
                log.info("????????????????????????,????????????,???????????????????????????,???????????????");
                orderStatusType = OrderStatusType.FAIL;
                chnResponse.setRetCode(ExceptionCodes.NETWORK_EXCEPTION);
                chnResponse.setRetMsg(ExceptionMessages.TRANSACTION_TIMEOUT_RETRY);
                chnResponse.setChnRetMsg("????????????,?????????.????????????,?????????????????????-JL");

                doCancel(outQrPayOrder);
            }

            completeResponse(chnResponse);
        }
        return orderStatusType;
    }

    private void doCancel(OutQrPayOrder outQrPayOrder) {
        log.info("??????????????????,??????????????????");
        DelayCancelMessage DelayCancelMessage = new DelayCancelMessage(qrcodeEngineProperties.getDelayCancelTime());
        DelayCancelMessage.setOutQrPayOrder(outQrPayOrder);
        delayCancelService.addToDelayQueue(DelayCancelMessage);
    }

    /**
     * ????????????   0-???????????????1-???????????????2-?????????3-??????,4-?????????/????????????5-?????????
     *
     * @param status
     */
    private void updateOrder(String status) {
        log.debug("??????:????????????????????? {}", OrderStatusType.getOutStautsType(status).name());
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
        log.debug("??????,????????????????????????");
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
