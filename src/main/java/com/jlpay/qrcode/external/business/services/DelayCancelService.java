package com.jlpay.qrcode.external.business.services;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.business.process.base.AbstractBusinessProcess;
import com.jlpay.qrcode.external.business.protocol.request.CancelTransRequest;
import com.jlpay.qrcode.external.business.services.protocol.DelayCancelMessage;
import com.jlpay.qrcode.external.business.services.protocol.request.ChannelBaseRequest;
import com.jlpay.qrcode.external.business.services.protocol.response.ChannelBaseResponse;
import com.jlpay.qrcode.external.commons.Constants;
import com.jlpay.qrcode.external.commons.io.client.ReactiveHttpTool;
import com.jlpay.qrcode.external.commons.util.DelayedExecutor;
import com.jlpay.qrcode.external.commons.util.DelayedTask;
import com.jlpay.qrcode.external.config.properties.QrcodeEngineProperties;
import com.jlpay.qrcode.external.controller.LocalBusiProcessContext;
import com.jlpay.qrcode.external.db.model.OrderStatusType;
import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import com.jlpay.qrcode.external.db.service.IOutQrPayOrderService;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.qrcode.external.support.QrcodeApiUtils;
import com.jlpay.transport.server.LocalContext;
import com.jlpay.utils.DateUtil;
import com.jlpay.utils.OsUtil;
import com.jlpay.utils.SpringContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: 延迟队列执行内部撤销
 * @author: lvlinyang
 * @date: 2019/11/13 10:58
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayCancelService {

    private final DelayedExecutor delayedExecutor;

    private final QrcodeEngineProperties qrcodeEngineProperties;

    private final ReactiveHttpTool httpTool;

    private final IOutQrPayOrderService outQrPayOrderService;

    private final ApplicationContext beanFactory;

    public void addToDelayQueue(DelayCancelMessage cancelMessage) {
        delayedExecutor.executeDelayedTask(DelayedTask.create(cancelMessage.getDelayMillis(), () -> {
            OutQrPayOrder outQrPayOrder = cancelMessage.getOutQrPayOrder();

            log.info("触发内部撤销操作");
            ChannelBaseResponse chnResp = doQuery(outQrPayOrder);
            String service;
            String prefix;
            if (OrderStatusType.SUCCESS.getEnginStatus().equals(chnResp.getStatus())) {
                log.info("渠道订单状态为成功,发起退款");
                //更新订单状态为成功
                outQrPayOrder.setStatus(OrderStatusType.SUCCESS.getOutStatus());
                outQrPayOrder.setRetCode(Constants.RET_CODE_SUCCESS);
                outQrPayOrder.setRetMsg(StringUtils.isNotEmpty(chnResp.getChnRetMsg()) ? chnResp.getChnRetMsg() : chnResp.getRetMsg());
                outQrPayOrder.setChnOrderNo(chnResp.getChnOrderId());

                outQrPayOrder.setSettleType(chnResp.getDiscountSettleType());
                outQrPayOrder.setFinnalAmount(QrcodeApiUtils.parseStrToLong(chnResp.getFinnalAmount()));
                outQrPayOrder.setDiscountAmount(QrcodeApiUtils.parseStrToLong(chnResp.getDiscountAmount()));
                outQrPayOrder.setSettleAmount(QrcodeApiUtils.parseStrToLong(chnResp.getSettlementAmt()));
                outQrPayOrder.setDiscountName(chnResp.getDiscountName());
                outQrPayOrder.setCouponInfo(chnResp.getCouponInfo());
                outQrPayOrder.setOpenid(chnResp.getSubOpenId());

                outQrPayOrderService.update(outQrPayOrder);

                service = ApiConstants.REFUND_SERVICE;
                prefix = "TK";
            } else {
                log.info("渠道订单状态非成功,发起撤销");
                service = ApiConstants.CANCEL_SERVICE;
                prefix = "CX";
            }
            reverseHandler(service, prefix, outQrPayOrder);
        }));
    }

    private ChannelBaseResponse doQuery(OutQrPayOrder outQrPayOrder) {
        log.debug("发起渠道查询,查询待撤销订单状态");
        ChannelBaseRequest channelBaseRequest = new ChannelBaseRequest();
        channelBaseRequest.setCommandId(ApiConstants.CHN_ORDER_QUERY);
        channelBaseRequest.setOrderId(outQrPayOrder.getOrderId());

        String response = httpTool.preparePost()
                .url(qrcodeEngineProperties.getUrl() + ApiConstants.CHN_ORDER_QUERY)
                .requestBody(channelBaseRequest)
                .execute()
                .block();

        return JSON.parseObject(response, ChannelBaseResponse.class);
    }

    private void reverseHandler(String service, String prefix, OutQrPayOrder outQrPayOrder) {
        log.debug("发起反向交易:{}", service);

        String outTradeNo = prefix + DateUtil.getLongDateString(new Date()) + RandomStringUtils.randomNumeric(9);
        try {
            CancelTransRequest cancelTransRequest = new CancelTransRequest();
            cancelTransRequest.setService(service);
            cancelTransRequest.setMchId(outQrPayOrder.getMerchNo());
            cancelTransRequest.setOrgCode(outQrPayOrder.getOrgCode());
            cancelTransRequest.setNonceStr(RandomStringUtils.randomNumeric(30));
            cancelTransRequest.setSigned("no");

            cancelTransRequest.setOutTradeNo(outTradeNo);
            cancelTransRequest.setOriOutTradeNo(outQrPayOrder.getOutOrderId());
            cancelTransRequest.setTotalFee(QrcodeApiUtils.parseLongToStr(outQrPayOrder.getAmount()));
            cancelTransRequest.setMchCreateIp(OsUtil.getIpAddress());
            cancelTransRequest.setRemark("同步被扫超时,外接码付内部发起反向交易处理:" + service);

            LocalBusiProcessContext localContext = new LocalBusiProcessContext();
            localContext.setRequestName(service);
            localContext.setLogId(outQrPayOrder.getOrderId());
            localContext.setRawRequest(JSON.toJSONString(cancelTransRequest));

            log.info("同步被扫超时,内部发起反向交易处理,请求报文:{}", JSON.toJSONString(localContext));
            AbstractBusinessProcess<?> busiProcess = beanFactory.getBean(service, AbstractBusinessProcess.class);
            busiProcess.process(localContext);
        } catch (Exception e) {
            log.error("内部反向交易处理异常", e);
        }
    }
}
