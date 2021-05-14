package com.jlpay.qrcode.external.business.dependency;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.business.dependency.protocol.request.OrderSystemCreateRequest;
import com.jlpay.qrcode.external.business.dependency.protocol.request.OrderSystemRequest;
import com.jlpay.qrcode.external.business.dependency.protocol.request.OrderSystemUpdateRequest;
import com.jlpay.qrcode.external.business.dependency.protocol.response.LMerchInfoResponse;
import com.jlpay.qrcode.external.business.dependency.protocol.response.OrderSystemCreateResponse;
import com.jlpay.qrcode.external.business.dependency.protocol.response.OrderSystemResponse;
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
import com.jlpay.transport.client.IClientCallback;
import com.jlpay.transport.client.thrift.SyncThriftService;
import com.jlpay.utils.DateUtil;
import com.jlpay.utils.FastJsonUtils;
import com.jlpay.utils.exception.BusiAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.MDC;
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
     * 从新订单系统获取订单号,若失败,则继续调用老订单系统获取
     *
     * @param baseTransRequest 基础请求类
     * @return 订单号
     */
    public String createOrderByV2(BaseTransRequest baseTransRequest) {
        log.info("创建新订单系统订单");
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
                    .perform()
                    .block();
            OrderSystemResponse response = JSON.parseObject(strResp, OrderSystemResponse.class);
            BusiAssert.isTrue(response.isSuccess() && StringUtils.isNotBlank(response.getOrderId()), strResp);
            orderId = response.getOrderId();
            if (StringUtils.isNotBlank(response.getCreateTime())) {
                baseTransRequest.setTransTime(DateUtil.parseDateNewFormat(response.getCreateTime()));
            }
        } catch (Exception e) {
            log.error("调用新订单系统生成订单号异常", e);
            eventReporter.prepareReport()
                    .level(EventLevel.NORMAL)
                    .requestName("create_orderSystem_order")
                    .code(Constants.BUSI_ERROR_CODE)
                    .msg("创建订单系统订单异常-" + e.getMessage())
                    .send();
            orderId = createOrder(baseTransRequest);
        }

        return orderId;
    }

    /**
     * 获取支付标识
     *
     * @param baseTransRequest 请求对象
     * @return 支付标识
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
     * 更新订单系统信息 - 当前仅用于三合一码,未扫码过期置为失败,同步状态至订单系统使用
     *
     * @param outQrPayOrder 外接码付流水
     */
    public void updateOrderSystem(OutQrPayOrder outQrPayOrder) {
        if (outQrPayOrder.getOrderId().length() != 24) {
            log.debug("非订单系统订单,不更新");
            return;
        }

        OrderStatusType outStautsType = OrderStatusType.getOutStautsType(outQrPayOrder.getStatus());
        if (!OrderStatusType.isFinalStatus(outStautsType)) {
            log.debug("非终态,不更新订单系统");
            return;
        }

        log.info("更新订单系统信息");
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
                .perform()
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
        log.error("更新订单系统异常", e);
        eventReporter.prepareReport()
                .level(EventLevel.NORMAL)
                .requestName("updateOrderSystem")
                .code(Constants.BUSI_ERROR_CODE)
                .msg("更新订单系统异常-" + e.getMessage())
                .send();
    }
}
