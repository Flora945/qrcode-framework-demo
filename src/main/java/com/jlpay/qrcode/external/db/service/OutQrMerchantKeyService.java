package com.jlpay.qrcode.external.db.service;

import com.jlpay.qrcode.external.db.mapper.OutQrMerchantKeyMapper;
import com.jlpay.qrcode.external.db.model.OutQrMerchantKey;
import com.jlpay.qrcode.external.support.SignUtil;
import com.jlpay.utils.exception.BusiAssert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qihuaiyuan
 * @date 2019/12/2 16:43
 */
@Slf4j
@Service
public class OutQrMerchantKeyService {

    @Resource
    private OutQrMerchantKeyMapper outQrMerchantKeyMapper;

    public String sign(String singContext, String orgCode) {
        BusiAssert.isTrue(StringUtils.isNotEmpty(orgCode), "加签的机构不能为空！");
        OutQrMerchantKey outQrMerchantKey = outQrMerchantKeyMapper.selectByPrimaryKey(orgCode);
        log.debug("商户私钥为:{}", outQrMerchantKey.getSysPriKey());
        return SignUtil.rsaSign(singContext, outQrMerchantKey.getSysPriKey());
    }

    public int deleteByPrimaryKey(String orgCode) {
        return outQrMerchantKeyMapper.deleteByPrimaryKey(orgCode);
    }


    public int insert(OutQrMerchantKey record) {
        return outQrMerchantKeyMapper.insert(record);
    }


    public int insertSelective(OutQrMerchantKey record) {
        return outQrMerchantKeyMapper.insertSelective(record);
    }


    public OutQrMerchantKey selectByPrimaryKey(String orgCode) {
        return outQrMerchantKeyMapper.selectByPrimaryKey(orgCode);
    }


    public int updateByPrimaryKeySelective(OutQrMerchantKey record) {
        return outQrMerchantKeyMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(OutQrMerchantKey record) {
        return outQrMerchantKeyMapper.updateByPrimaryKey(record);
    }

}
