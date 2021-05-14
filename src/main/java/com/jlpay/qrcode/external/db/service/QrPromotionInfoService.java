package com.jlpay.qrcode.external.db.service;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.business.protocol.request.BaseTransRequest;
import com.jlpay.qrcode.external.db.mapper.QrPromotionInfoMapper;
import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import com.jlpay.qrcode.external.db.model.QrPromotionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @Description: 码付优惠信息服务
 * @author: lvlinyang
 * @date: 2020/10/28 15:23
 */
@Slf4j
@Service
public class QrPromotionInfoService {

    @Autowired
    private QrPromotionInfoMapper qrPromotionInfoMapper;

    public QrPromotionInfo savePromotionInfo(BaseTransRequest baseTransRequest, OutQrPayOrder outQrPayOrder) {
        QrPromotionInfo qrPromotionInfo = new QrPromotionInfo();
        qrPromotionInfo.setOrderId(outQrPayOrder.getOrderId());
        qrPromotionInfo.setGoodsTag(baseTransRequest.getGoodsTag());
        qrPromotionInfo.setGoodsData(baseTransRequest.getGoodsData());
        save(qrPromotionInfo);
        return qrPromotionInfo;
    }

    public void save(QrPromotionInfo qrPromotionInfo) {
        long startTime = System.currentTimeMillis();
        qrPromotionInfo.setCreateTime(new Date());
        qrPromotionInfoMapper.save(qrPromotionInfo);
        log.info("保存优惠信息:{},耗时:{}ms", JSON.toJSONString(qrPromotionInfo), System.currentTimeMillis() - startTime);
    }

    public QrPromotionInfo queryByOrderId(String orderId) {
        long startTime = System.currentTimeMillis();
        QrPromotionInfo qrPromotionInfo = qrPromotionInfoMapper.queryByOrderId(orderId);
        log.info("根据orderId查询优惠信息耗时:{}ms", System.currentTimeMillis() - startTime);
        return qrPromotionInfo;
    }

    public void update(QrPromotionInfo qrPromotionInfo) {
        long startTime = System.currentTimeMillis();
        qrPromotionInfo.setUpdateTime(new Date());
        qrPromotionInfoMapper.update(qrPromotionInfo);
        log.info("更新订单优惠信息:{},耗时:{}ms", JSON.toJSONString(qrPromotionInfo), System.currentTimeMillis() - startTime);
    }

}
