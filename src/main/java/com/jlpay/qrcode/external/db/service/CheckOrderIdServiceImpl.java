package com.jlpay.qrcode.external.db.service;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.db.mapper.CheckOrderIdMapper;
import com.jlpay.qrcode.external.db.model.CheckOrderId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckOrderIdServiceImpl implements ICheckOrderIdService {

    @Autowired
    private CheckOrderIdMapper checkOrderIdMapper;

    @Override
    public void save(CheckOrderId checkOrderId) {
        long startTime=System.currentTimeMillis();
        checkOrderIdMapper.save(checkOrderId);
        log.info("保存商户订单号效验信息:{},耗时:{}ms",JSON.toJSONString(checkOrderId),System.currentTimeMillis()-startTime);
    }
}
