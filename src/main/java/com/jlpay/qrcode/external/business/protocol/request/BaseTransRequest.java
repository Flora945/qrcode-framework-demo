package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.dependency.protocol.response.LMerchInfoResponse;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 基础请求类
 *
 * @author lvlinyang
 * @date 2019/10/16 17:05
 */
@Data
@Slf4j
public class BaseTransRequest {
    /**
     * 接口名称
     */
    private String service;
    /**
     * 嘉联商户号
     */
    @JSONField(name = "mch_id")
    private String mchId;
    /**
     * 嘉联机构号
     */
    @JSONField(name = "org_code")
    private String orgCode;
    /**
     * 签名
     */
    private String sign;
    /**
     * 随机串
     */
    @JSONField(name = "nonce_str")
    private String nonceStr;
    /**
     * 版本
     */
    private String version;
    /**
     * 字符集
     */
    private String charset;
    /**
     * 签名类型
     */
    @JSONField(name = "sign_type")
    private String signType;

    /**
     * 交易类型<br>
     * 微信支付 : wxpay<br>
     * 支付宝 : alipay<br>
     * 银联二维码 : unionpay<br>
     * QQ支付 : qqpay<br>
     * 嘉联聚合支付: jlpay
     */
    @JSONField(name = "pay_type")
    private String payType;
    /**
     * 商家订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 交易金额,单位: 分
     */
    @JSONField(name = "total_fee")
    private String totalFee;
    /**
     * 商品标题
     */
    private String body;
    /**
     * 终端号
     */
    @JSONField(name = "term_no")
    private String termNo;
    /**
     * 终端设备号
     */
    @JSONField(name = "device_info")
    private String deviceInfo;
    /**
     * 终端IP
     */
    @JSONField(name = "mch_create_ip")
    private String mchCreateIp;

    /**
     * 商品描述
     */
    private String attach;

    /**
     * 分期标识<br>
     * 0-否  1-是（交易类型alipay）
     */
    @JSONField(name = "is_hire_purchase")
    private String isHirePurchase;
    /**
     * 分期数，可选值3、6、12（交易类型alipay）
     */
    @JSONField(name = "hire_purchase_num")
    private String hirePurchaseNum;
    /**
     * 商家承担的手续费比例,目前只支持传0
     */
    @JSONField(name = "hire_purchase_seller_percent")
    private String hirePurchaseSellerPercent;
    /**
     * 是否分账 0-否  1-是
     */
    @JSONField(name = "profit_sharing")
    private String profitSharing;

    /**
     * 备注
     */
    private String remark;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;

    /**
     * 操作员:商家收银员编号
     */
    @JSONField(name = "op_user_id")
    private String opUserId;
    /**
     * 门店号:商家门店号
     */
    @JSONField(name = "op_shop_id")
    private String opShopId;
    /**
     * 公众账号appid
     */
    @JSONField(name = "sub_appid")
    private String subAppid;

    /**
     * 回调通知地址
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * 来源：0-外部，1-内部
     */
    @JSONField(name = "source")
    private String source;

    /**
     * 支付有效时间,单位:分
     */
    @JSONField(name = "payment_valid_time")
    private String paymentValidTime;

    /**
     * 业务大类
     */
    @JSONField(name = "busi_type")
    private String busiType;
    /**
     * 业务小类
     */
    @JSONField(name = "busi_sub_type")
    private String busiSubType;
    /**
     * 交易类型
     */
    @JSONField(name = "trade_type")
    private String tradeType;

    /**
     * 交易时间
     */
    private Date transTime;

    /**
     * 外接码付版本<br>
     * 重构新外接码付: V3.0<br>
     * 重构老外接码付: V1.5
     */
    @JSONField(name = "qrcode_version")
    private String qrcodeVersion;

    /**
     * 包含此字段,则不做加解签处理
     */
    private String signed;
    /**
     * 商家原交易订单号
     */
    @JSONField(name = "ori_out_trade_no")
    private String oriOutTradeNo;

    /**
     * 原交易嘉联订单号
     */
    @JSONField(name = "ori_transaction_id")
    private String oriTransactionId;

    /**
     * 支付限制 no_credit-禁止使用信用卡
     */
    @JSONField(name = "limit_pay")
    private String limitPay;

    /**
     * 订单优惠标记,用于区分订单是否可以享受优惠，字段内容在微信后台配置券时进行设置
     */
    @JSONField(name = "goods_tag")
    private String goodsTag;

    /**
     * 商品详情,微信，支付宝,银联二维码的单品优惠活动时该字段必传，JSON格式
     */
    @JSONField(name = "goods_data")
    private String goodsData;

    /**
     * 前台成功通知地址,目前仅用于银联二维码JS支付
     */
    @JSONField(name = "front_url")
    private String frontUrl;

    /**
     * 前台失败通知地址,目前仅用于银联二维码JS支付
     */
    @JSONField(name = "front_fail_url")
    private String frontFailUrl;

    /**
     * 左端商户信息
     */
    private LMerchInfoResponse lMerchInfo;

    public void validate() {
        log.debug("效验请求参数格式");
        ParamAssert.notBlank(service, "接口类型不能为空");
        ParamAssert.notBlank(mchId, "商户号不能为空");
        ParamAssert.notBlank(orgCode, "机构号不能为空");
        if (StringUtils.isEmpty(signed)) {
            ParamAssert.notBlank(sign, "签名不能为空");
        }
        ParamAssert.notBlank(nonceStr, "随机字符串不能为空");

        if (StringUtils.isNotBlank(mchCreateIp)) {
            ParamAssert.isTrue(Pattern.matches("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$",
                    getMchCreateIp())
                    || Pattern.matches("^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$",
                    getMchCreateIp()), "非法ip");
        }

        //长度效验
        validateParamsLength(service, 32);
        validateParamsLength(mchId, 32);
        validateParamsLength(orgCode, 32);
        validateParamsLength(nonceStr, 32);
        validateParamsLength(version, 8);
        validateParamsLength(charset, 8);
        validateParamsLength(signType, 8);

        validateParamsLength(payType, 10);
        validateParamsLength(outTradeNo, 32);
        validateParamsLength(totalFee, 15);
        validateParamsLength(body, 128);
        validateParamsLength(termNo, 8);
        validateParamsLength(deviceInfo, 32);

        validateParamsLength(attach, 500);
        validateParamsLength(isHirePurchase, 1);
        validateParamsLength(hirePurchaseNum, 2);
        validateParamsLength(hirePurchaseSellerPercent, 3);
        validateParamsLength(profitSharing, 1);

        validateParamsLength(remark, 256);
        validateParamsLength(longitude, 20);
        validateParamsLength(latitude, 20);
        validateParamsLength(opUserId, 32);
        validateParamsLength(opShopId, 32);
        validateParamsLength(subAppid, 30);
        validateParamsLength(tradeType, 20);

        validateParamsLength(notifyUrl, 225);
        validateParamsLength(limitPay, 64);
        validateParamsLength(goodsTag, 32);
    }

    /**
     * 优先取终端号,没有,再取终端设备号,都没,则送默认值
     *
     * @return 获取终端号
     */
    public String getTermNoOrDeviceInfo() {
        if (StringUtils.isNotBlank(termNo)) {
            return termNo;
        }
        if (StringUtils.isNotBlank(deviceInfo)) {
            return getDeviceInfoFormat(deviceInfo);
        }
        return "56767676";
    }

    private String getDeviceInfoFormat(String deviceInfo) {
        if (deviceInfo.length() > 8) {
            return deviceInfo.substring(0, 8);
        }
        if (deviceInfo.length() < 8) {
            return StringUtils.leftPad(deviceInfo, 8, "0");
        }
        return deviceInfo;
    }

    /**
     * 效验参数长度合理性
     *
     * @param params 参数
     * @param length 长度
     */
    public void validateParamsLength(String params, int length) {
        if (StringUtils.isNotBlank(params)) {
            ParamAssert.isTrue(params.length() <= length, "{}-长度超过限制", params);
        }
    }

    public String getMchCreateIp() {
        if (StringUtils.isNotBlank(mchCreateIp)) {
            return mchCreateIp.split(",")[0];
        }
        return mchCreateIp;
    }
}
