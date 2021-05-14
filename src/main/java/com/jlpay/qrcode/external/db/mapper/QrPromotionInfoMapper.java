package com.jlpay.qrcode.external.db.mapper;

import com.jlpay.qrcode.external.db.model.QrPromotionInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QrPromotionInfoMapper {

    void save(QrPromotionInfo qrPromotionInfo);

    QrPromotionInfo queryByOrderId(String orderId);

    void update(QrPromotionInfo qrPromotionInfo);

}
