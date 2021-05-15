package com.jlpay.qrcode.external.db.service;

import com.jlpay.qrcode.external.business.services.protocol.response.ChannelTranNotifyResponse;
import com.jlpay.qrcode.external.business.protocol.request.BaseTransRequest;
import com.jlpay.qrcode.external.business.protocol.request.RefundTransRequest;
import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import com.jlpay.qrcode.external.db.model.OutQrStaticQrcode;

import java.util.List;

public interface IOutQrPayOrderService {

    OutQrPayOrder createOrder(BaseTransRequest transCodeRequest);

    OutQrPayOrder createReverseOrder(RefundTransRequest refundTransRequest, OutQrPayOrder oriOrder, String busiSubType);

    OutQrPayOrder createUnionpayOrder(ChannelTranNotifyResponse notifyMessage, OutQrStaticQrcode staticQrcode);

    void save(OutQrPayOrder payOrder);

    void update(OutQrPayOrder payOrder);

    OutQrPayOrder findById(String orderId);

    OutQrPayOrder findByOutTradeNo(String orgCode,String outTradeNo);

    List<OutQrPayOrder> findByOriOrderId(String oriOrderId, String status);
}
