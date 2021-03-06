package com.jlpay.qrcode.external.business.services;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.business.services.protocol.request.OrderSystemCreateRequest;
import com.jlpay.qrcode.external.business.services.protocol.request.OrderSystemRequest;
import com.jlpay.qrcode.external.business.services.protocol.request.OrderSystemUpdateRequest;
import com.jlpay.qrcode.external.business.services.protocol.response.LMerchInfoResponse;
import com.jlpay.qrcode.external.business.services.protocol.response.OrderSystemCreateResponse;
import com.jlpay.qrcode.external.business.services.protocol.response.OrderSystemResponse;
import com.jlpay.qrcode.external.business.protocol.request.BaseTransRequest;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.business.protocol.enums.OrderSystemStatusType;
import com.jlpay.qrcode.external.business.protocol.request.MicropayPreAuthRequest;
import com.jlpay.qrcode.external.business.protocol.request.MicropayRequest;
import com.jlpay.qrcode.external.commons.events.EventLevel;
import com.jlpay.qrcode.external.commons.events.EventReporter;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.io.client.ReactiveHttpTool;
import com.jlpay.qrcode.external.config.properties.OrderSystemProperties;
import com.jlpay.qrcode.external.db.model.OrderStatusType;
import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.qrcode.external.support.Constants;
import com.jlpay.transport.client.thrift.SyncThriftService;
import com.jlpay.utils.DateUtil;
import com.jlpay.utils.FastJsonUtils;
import com.jlpay.utils.exception.BusiAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


/**
 * @author lujianyuan
 * @author lvlinyang
 * @since 2019/10/11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSystemService {

    @Value("${jlpay.commons.system.order.sources}")
    private String orderSystemSources;

    @Value("${jlpay.commons.system.order.timeout}")
    private int timeout;

    @Value("${jlpay.commons.system.order.register-path}")
    private String registerNode;

    private final SyncThriftService syncThriftService;

    private final OrderSystemProperties orderSystemProperties;

    private final EventReporter eventReporter;

    private final ReactiveHttpTool httpTool;

    public String createOrder(BaseTransRequest baseTransRequest) {
        OrderSystemCreateRequest createRequest = new OrderSystemCreateRequest();
        createRequest.setAmount(baseTransRequest.getTotalFee());
        createRequest.setBusiType(baseTransRequest.getBusiType());
        createRequest.setBusiSubType(baseTransRequest.getBusiSubType());
        createRequest.setMerchNo(baseTransRequest.getMchId());
        createRequest.setTransTime(baseTransRequest.getTransTime());
        createRequest.setRequestId(baseTransRequest.getOutTradeNo());
        createRequest.setSources(orderSystemSources);
        createRequest.setCommandId("create_order");
        String responseStr = syncThriftService.invoke(registerNode, FastJsonUtils.toJSONString(createRequest), timeout);
        OrderSystemCreateResponse response = JSON.parseObject(responseStr, OrderSystemCreateResponse.class);
        BusiAssert.isTrue("00".equals(response.getRetCode()), ExceptionCodes.SYSTEM_EXCEPTION, "{}[ORDER.CREATE.FAIL.{}]", response.getRetMsg(), response.getRetCode());
        return response.getOrderId();
    }

    /**
     * ?????????????????????????????????,?????????,????????????????????????????????????
     *
     * @param baseTransRequest ???????????????
     * @return ?????????
     */
    public String createOrderByV2(BaseTransRequest baseTransRequest) {
        log.info("???????????????????????????");
        OrderSystemRequest orderSystemRequest = new OrderSystemRequest();
        orderSystemRequest.setMerchNo(baseTransRequest.getMchId());
        orderSystemRequest.setAmount(baseTransRequest.getTotalFee());
        orderSystemRequest.setSources(ApiConstants.SERVICE_NAME);
        orderSystemRequest.setStatus(OrderStatusType.WAIT.getOutStatus());
        orderSystemRequest.setBusiType(baseTransRequest.getBusiType());
        orderSystemRequest.setTransType(BusiSubType.forValue(baseTransRequest.getBusiSubType()).getOrderSystemType());
        orderSystemRequest.setOriOrderId(baseTransRequest.getOriTransactionId());
        orderSystemRequest.setOutOrderId(baseTransRequest.getOutTradeNo());
        orderSystemRequest.setPayToken(convertPayToken(baseTransRequest));
        if (StringUtils.isNotBlank(baseTransRequest.getPayType()) && !ApiConstants.JLPAY.equals(baseTransRequest.getPayType())) {
            orderSystemRequest.setPayType(baseTransRequest.getPayType());
        }

        String location = StringUtils.isNotBlank(baseTransRequest.getMchCreateIp()) ? baseTransRequest.getMchCreateIp() : "unknown";
        orderSystemRequest.setLocation(location);
        orderSystemRequest.setRemark(baseTransRequest.getRemark());
        orderSystemRequest.setValidTime(baseTransRequest.getPaymentValidTime() + "M");
        orderSystemRequest.setTermSn(baseTransRequest.getDeviceInfo());

        LMerchInfoResponse lMerchInfo = baseTransRequest.getLMerchInfo();
        if (lMerchInfo != null) {
            orderSystemRequest.setPrintMerchName(lMerchInfo.getPrintMerchName());
            orderSystemRequest.setAgentId(lMerchInfo.getAgentId());

            orderSystemRequest.setAreaCode(StringUtils.isNotBlank(lMerchInfo.getRegionCode()) ? lMerchInfo.getRegionCode() : "unknown");
        } else {
            orderSystemRequest.setAreaCode("unknown");
        }

        orderSystemRequest.setTermNo(baseTransRequest.getTermNoOrDeviceInfo());

        String orderId;
        try {
            String strResp = httpTool.preparePost()
                    .url(orderSystemProperties.getUrlPrefix() + "/order/create")
                    .requestBody(JSON.toJSONString(orderSystemRequest))
                    .requestTimeout(orderSystemProperties.getReadTimeout())
                    .execute()
                    .block();
            OrderSystemResponse response = JSON.parseObject(strResp, OrderSystemResponse.class);
            BusiAssert.isTrue(response.isSuccess() && StringUtils.isNotBlank(response.getOrderId()), strResp);
            orderId = response.getOrderId();
            if (StringUtils.isNotBlank(response.getCreateTime())) {
                baseTransRequest.setTransTime(DateUtil.parseDateNewFormat(response.getCreateTime()));
            }
        } catch (Exception e) {
            log.error("??????????????????????????????????????????", e);
            eventReporter.prepareReport()
                    .level(EventLevel.NORMAL)
                    .requestName("create_orderSystem_order")
                    .code(Constants.BUSI_ERROR_CODE)
                    .msg("??????????????????????????????-" + e.getMessage())
                    .send();
            orderId = createOrder(baseTransRequest);
        }

        return orderId;
    }

    /**
     * ??????????????????
     *
     * @param baseTransRequest ????????????
     * @return ????????????
     */
    private String convertPayToken(BaseTransRequest baseTransRequest) {
        String payToken = null;
        if (baseTransRequest instanceof MicropayRequest) {
            MicropayRequest micropayRequest = (MicropayRequest) baseTransRequest;
            payToken = micropayRequest.getAuthCode();
        } else if (baseTransRequest instanceof MicropayPreAuthRequest) {
            MicropayPreAuthRequest micropayPreAuthRequest = (MicropayPreAuthRequest) baseTransRequest;
            payToken = micropayPreAuthRequest.getAuthCode();
        }

        return payToken;
    }

    /**
     * ???????????????????????? - ???????????????????????????,???????????????????????????,?????????????????????????????????
     *
     * @param outQrPayOrder ??????????????????
     */
    public void updateOrderSystem(OutQrPayOrder outQrPayOrder) {
        if (outQrPayOrder.getOrderId().length() != 24) {
            log.debug("?????????????????????,?????????");
            return;
        }

        OrderStatusType outStautsType = OrderStatusType.getOutStautsType(outQrPayOrder.getStatus());
        if (!OrderStatusType.isFinalStatus(outStautsType)) {
            log.debug("?????????,?????????????????????");
            return;
        }

        log.info("????????????????????????");
        OrderSystemUpdateRequest updateRequest = new OrderSystemUpdateRequest();
        updateRequest.setOrderId(outQrPayOrder.getOrderId());
        updateRequest.setAuthCode(outQrPayOrder.getAuthCode());
        updateRequest.setStatus(OrderSystemStatusType.convertStatus(outQrPayOrder.getStatus()));
        updateRequest.setRetCode(outQrPayOrder.getRetCode());
        updateRequest.setRetMsg(outQrPayOrder.getRetMsg());
        updateRequest.setTransTime(DateUtil.getNewFormatDateString(outQrPayOrder.getTransTime()));

        httpTool.preparePost()
                .url(orderSystemProperties.getUrlPrefix() + "/order/update")
                .contentType(MediaType.APPLICATION_JSON)
                .requestBody(JSON.toJSONString(updateRequest))
                .requestTimeout(orderSystemProperties.getReadTimeout())
                .execute()
                .doOnError(this::handleUpdateException)
                .subscribe(result -> {
                    try {
                        OrderSystemResponse response = JSON.parseObject(result, OrderSystemResponse.class);
                        BusiAssert.isTrue(response.isSuccess(), result);
                    } catch (Exception e) {
                        handleUpdateException(e);
                    }
                });
    }

    private void handleUpdateException(Throwable e) {
        log.error("????????????????????????", e);
        eventReporter.prepareReport()
                .level(EventLevel.NORMAL)
                .requestName("updateOrderSystem")
                .code(Constants.BUSI_ERROR_CODE)
                .msg("????????????????????????-" + e.getMessage())
                .send();
    }
}
