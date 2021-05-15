package com.jlpay.qrcode.external.business.services.protocol.request.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 码付预授权(B2C)请求接口协议
 * <p>
 * Created by wangyuhai1 on 2019/10/31.
 */
@Data
public class QrcodeAuthB2cRequest extends QrcodeAuthBaseRequest {

    public static final String COMMAND_ID = "QrcodeAuthB2c";

    @JSONField(name = "command_id")
    private String commandId = COMMAND_ID;
    /**
     * 担保有效时间，单位为天
     **/
    @JSONField(name = "guarantee_expiry")
    private String guaranteeExpiry;
    /**
     * 是否实名:1-实名，其他-非实名
     **/
    @JSONField(name = "cert_flag")
    private String certFlag;
    /**
     * 证件类型
     **/
    @JSONField(name = "cert_type")
    private String certType;
    /**
     * 证件号码
     **/
    @JSONField(name = "cert_no")
    private String certNo;
    /**
     * 真实姓名
     **/
    @JSONField(name = "cert_name")
    private String certName;
    /**
     * 交易手机号
     **/
    @JSONField(name = "phone_no")
    private String phoneNo;
    /**
     * 分期标识。是否分期付款,当前仅支付支付宝花呗分期 1-是 其他-否
     **/
    @JSONField(name = "is_hire_purchase")
    private String isHirePurchase;
    /**
     * 分期数：只能是3、6、12
     **/
    @JSONField(name = "hire_purchase_num")
    private String hirePurchaseNum;
    /**
     * 卖家承担的手续费比例，分期付款需要卖家承担的手续费比例的百分值，传入100 代表100%（交易类型为alipay，目前只支持传0）
     **/
    @JSONField(name = "hire_purchase_seller_percent")
    private String hirePurchaseSellerPercent;
    /**
     * 微信用户标识,用户在商户appid下的唯一标识
     **/
    @JSONField(name = "open_id")
    private String openId;
    /**
     * 公众账号ID,微信分配的公众账号ID
     **/
    @JSONField(name = "sub_appid")
    private String subAppId;
    /**
     * 买家支付宝账号,pay_type=alipay时，buyer_logon_id和buyer_id不能同时为空
     **/
    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;
    /**
     * 买家支付宝用户ID，pay_type=alipay时，buyer_logon_id和buyer_id不能同时为空
     **/
    @JSONField(name = "buyer_id")
    private String buyerId;
    /**
     * 用户授权码（机构和银联交互获得），pay_type=unionpay时，必须填写
     **/
    @JSONField(name = "user_auth_code")
    private String userAuthCode;
    /**
     * 银联支付标识，银联支付标识（机构和银联交互获得），pay_type=unionpay时，必须填写
     **/
    @JSONField(name = "app_up_identifier")
    private String appUpIdentifier;

    @JSONField(name = "payment_valid_time")
    private String paymentValidTime;
}
