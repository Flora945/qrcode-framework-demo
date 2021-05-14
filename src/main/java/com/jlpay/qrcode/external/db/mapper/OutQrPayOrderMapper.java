package com.jlpay.qrcode.external.db.mapper;

import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OutQrPayOrderMapper {

    void save(OutQrPayOrder outQrPayOrder);

    OutQrPayOrder queryByOutTradeNo(@Param("orgCode") String orgCode,@Param("outTradeNo") String outTradeNo);

    OutQrPayOrder queryByOrderId(@Param("orderId") String orderId);

    List<OutQrPayOrder> queryByOriOrderId(@Param("oriOrderId") String oriOrderId, @Param("status") String status);

    void update(OutQrPayOrder outQrPayOrder);
}
