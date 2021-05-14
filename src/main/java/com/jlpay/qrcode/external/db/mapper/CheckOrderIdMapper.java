package com.jlpay.qrcode.external.db.mapper;

import com.jlpay.qrcode.external.db.model.CheckOrderId;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CheckOrderIdMapper {

    void save(CheckOrderId checkOrderId);

}
