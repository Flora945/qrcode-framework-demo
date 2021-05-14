package com.jlpay.qrcode.external.db.mapper;

import com.jlpay.qrcode.external.db.model.OutQrStaticQrcode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qihuaiyuan
 * @since 2019/9/3 16:31
 */
@Mapper
public interface OutQrStaticQrcodeMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OutQrStaticQrcode record);

    int insertSelective(OutQrStaticQrcode record);

    OutQrStaticQrcode selectByPrimaryKey(String orderId);

    OutQrStaticQrcode getValidNfcTag(String merchNo, String termNo, String status);

    int updateByPrimaryKeySelective(OutQrStaticQrcode record);

    int updateByPrimaryKey(OutQrStaticQrcode record);

}