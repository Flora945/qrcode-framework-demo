package com.jlpay.qrcode.external.db.mapper;

import com.jlpay.qrcode.external.db.model.OutQrMerchantKey;
import org.apache.ibatis.annotations.Mapper;

/**
  *
  * @author qihuaiyuan
  * @date 2019/12/2 16:43
  */
@Mapper
public interface OutQrMerchantKeyMapper {
    int deleteByPrimaryKey(String orgCode);

    int insert(OutQrMerchantKey record);

    int insertSelective(OutQrMerchantKey record);

    OutQrMerchantKey selectByPrimaryKey(String orgCode);

    int updateByPrimaryKeySelective(OutQrMerchantKey record);

    int updateByPrimaryKey(OutQrMerchantKey record);

}