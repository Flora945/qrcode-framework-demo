package com.jlpay.qrcode.external.db.service;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.db.mapper.OutQrStaticQrcodeMapper;
import com.jlpay.qrcode.external.db.model.OutQrStaticQrcode;
import com.jlpay.qrcode.api.protocol.enums.OutQrStaticQrcodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author qihuaiyuan
 * @since 2019/9/3 16:31
 */
@Service
@Slf4j
public class OutQrStaticQrcodeServiceImpl implements IOutQrStaticQrcodeService{

    @Autowired
    private OutQrStaticQrcodeMapper outQrStaticQrcodeMapper;

    @Override
    public int insert(OutQrStaticQrcode record) {
        record.setCreateTime(new Date());
        log.info("保存静态码信息:{}",JSON.toJSONString(record));
        return outQrStaticQrcodeMapper.insert(record);
    }

    @Override
    public int saveNotNull(OutQrStaticQrcode record) {
        record.setCreateTime(new Date());
        log.info("保存非空静态码信息:{}",JSON.toJSONString(record));
        return outQrStaticQrcodeMapper.insertSelective(record);
    }

    @Override
    public OutQrStaticQrcode getByOrderId(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return null;
        }
        return outQrStaticQrcodeMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public int updateNotNull(OutQrStaticQrcode record) {
        record.setUpdateTime(new Date());
        log.info("更新非空静态码信息:{}",JSON.toJSONString(record));
        return outQrStaticQrcodeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OutQrStaticQrcode record) {
        record.setUpdateTime(new Date());
        log.info("根据主键更新静态码信息:{}",JSON.toJSONString(record));
        return outQrStaticQrcodeMapper.updateByPrimaryKey(record);
    }

    /**
     * 获取指定商户,终端对应的Nfc标签数据
     *
     * @param merchNo 商户号
     * @param termNo  终端号
     * @return Nfc标签数据
     */
    @Override
    public OutQrStaticQrcode getValidNfcTag(String merchNo, String termNo) {
        return outQrStaticQrcodeMapper.getValidNfcTag(merchNo, termNo, OutQrStaticQrcodeEnum.STATUS_SUCCESS.getValue());
    }

}
