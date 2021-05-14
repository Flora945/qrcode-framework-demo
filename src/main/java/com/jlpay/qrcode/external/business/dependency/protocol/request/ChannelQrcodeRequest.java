package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class ChannelQrcodeRequest extends com.jlpay.qrcode.api.protocol.channel.request.ChannelBaseRequest {

    /**标题*/
    private String subject;
    /**标题详细信息*/
    @JSONField(name = "subject_detail")
    private String subjectDetail;
    /**交易IP*/
    @JSONField(name = "trans_ip")
    private String transIp;

    /***===================实名支付字段=================**/
    /**是否实名支付(1 - 是)*/
    @JSONField(name = "cert_flag")
    private String certFlag;
    /**证件类型*/
    @JSONField(name = "cert_type")
    private String certType;
    /**证件号*/
    @JSONField(name = "cert_no")
    private String certNo;
    /***真实姓名*/
    @JSONField(name = "cert_name")
    private String certName;
    /**交易手机号*/
    @JSONField(name = "phone_no")
    private String phoneNo;
    /***===================实名支付字段=================**/

    /**支付有效时间,单位:分*/
    @JSONField(name = "payment_valid_time")
    private String paymentValidTime;
    /**设备编号*/
    @JSONField(name = "op_device_id")
    private String opDeviceId;
    /**门店所属城市*/
    @JSONField(name = "area_info")
    private String areaInfo;
    /**单品优惠信息*/
    @JSONField(name = "goods_detail")
    private String goodsDetail;
    /**商品标记*/
    @JSONField(name = "goods_tag")
    private String goodsTag;
    /**产品 ID,微信、支付宝直接使用*/
    @JSONField(name = "product_id")
    private String productId;
    /**门店编号**/
    @JSONField(name = "op_shop_id")
    private String opShopId;
    /**操作员*/
    @JSONField(name = "op_user_id")
    private String opUserId;

    /***===================花呗分期字段=================**/
    /**分期标识。是否分期付款,当前仅支付支付宝花呗分期 0-否 1-是*/
    @JSONField(name = "is_hire_purchase")
    private String isHirePurchase;
    /**分期数，可选值3、6、12*/
    @JSONField(name = "hire_purchase_num")
    private String hirePurchaseNum;
    /**卖家承担的手续费比例，分期付款需要卖家承担的手续费比例的百分值，传入100 代表100%（交易类型为alipay，目前只支持传0）*/
    @JSONField(name = "hire_purchase_seller_percent")
    private String hirePurchaseSellerPercent;
    /***系统商编号*/
    @JSONField(name = "sys_service_provider_id")
    private String sysServiceProviderId;
    /***行业数据回流信息*/
    @JSONField(name = "industry_reflux_info")
    private String industryRefluxInfo;
    /**卡类型*/
    @JSONField(name = "card_type")
    private String cardType;
    /***===================花呗分期字段=================**/

    /**地理位置经度*/
    private String longitude;
    /**地理位置纬度*/
    private String latitude;
    /**微信公众号id*/
    @JSONField(name = "sub_appid")
    private String subAppid;
    /**订单生成时间 yyyy-MM-dd HH:mm:ss*/
    @JSONField(name = "time_start",format = "yyyy-MM-dd HH:mm:ss")
    private Date timeStart;

    /**
     * 生成银联静态码所需字段
     */
    /***设备号*/
    @JSONField(name = "device_sn")
    private String deviceSn;

    /**标签ID*/
    @JSONField(name="nfc_tag_id")
    private String nfcTagId;
    /**旧的标签ID*/
    @JSONField(name = "old_nfc_tag_id")
    private String oldNfcTagId;

    /**限制支付类型<br/>
     * 当前用于微信主扫生成三合一码时,限制只允许微信支付*/
    @JSONField(name = "limit_pay_type")
    private String limitPayType;
}
