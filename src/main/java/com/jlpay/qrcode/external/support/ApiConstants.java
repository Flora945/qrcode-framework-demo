package com.jlpay.qrcode.external.support;

/**
 * @author lvlinyang
 * @since 2019/11/12
 */
public class ApiConstants {

    public static final String SUCCESS_CODE = "00";

    /**
     * 重构新外接码付版本号
     */
    public static final String VERSION = "V3.0";
    /**
     * 重构老外接码付版本号
     */
    public static final String OLD_VERSION = "V1.5";
    /**
     * 服务名
     */
    public static final String SERVICE_NAME = "ext-qrcode-api";

    /**
     * 发送至消息推送服务的标识
     */
    public static final String NOTIFY_TYPE = "external_qrcode";
    public static final String NOTIFY_TYPE_V15 = "openapi_trans";
    public static final String NOTIFY_TYPE_JSAPI = "jsapi_code";
    /**
     * 发送至三合一的来源标识
     */
    public static final String NATIVE_PACKAGE_SOURCE = "ext";
    /**
     * 机构号
     */
    public static final String ORG_CODE = "org_code";
    /**
     * 商户号
     */
    public static final String MCH_ID = "mch_id";


    public static final String SERVICE_PREFIX = "pay.qrcode.";

    /**
     * 预授权完成
     */
    public static final String PRE_AUTHORIZATION_COMPLETE_SERVICE = "pay.qrcode.preauthcomplete";

    /**
     * 预授权撤销
     */
    public static final String PRE_AUTHORIZATION_REVOKE_SERVICE = "pay.qrcode.preauthrevoke";

    /**
     * 被扫异步接口
     **/
    public static final String MICROPAYASYN_SERVICE = "pay.qrcode.micropayasyn";
    /***被扫同步等待结果接口**/
    public static final String MICROPAY_SERVICE = "pay.qrcode.micropay";
    /**
     * 被扫同步担保交易
     */
    public static final String MICROPAY_PA_SERVICE = "pay.qrcode.micropaypreauth";
    /***主扫接口**/
    public static final String QRCODEPAY_SERVICE = "pay.qrcode.qrcodepay";
    /**
     * 主扫担保
     */
    public static final String QRCODEPAY_PA_SERVICE = "pay.qrcode.qrcodepaypreauth";

    public static final String PRE_AUTHENTICATION_QUERY = "pay.qrcode.preauthquery";

    /**
     * 撤销订单接口
     */
    public static final String CANCEL_SERVICE = "pay.qrcode.cancel";
    public static final String PRE_AUTH_TRANS_CLOSE_SERVICE = "pay.qrcode.preauthclose";
    /**
     * 退款接口
     */
    public static final String REFUND_SERVICE = "pay.qrcode.refund";
    /**
     * 支付宝服务窗接口
     **/
    public static final String WAPH5PAY_SERVICE = "pay.qrcode.waph5pay";
    /***绑定支付目录接口*/
    public static final String AUTHBIND_SERVICE = "pay.qrcode.authbind";
    /***根据授权码获取用户ID**/
    public static final String GETOPENID_SERVICE = "pay.qrcode.getopenid";
    /**
     * 交易订单查询接口
     */
    public static final String CHNORDERQUERY_SERVICE = "pay.qrcode.chnquery";
    /**
     * 交易订单查询接口
     */
    public static final String ORDERQUERY_SERVICE = "pay.qrcode.query";
    /***公众号支付接口*/
    public static final String OFFICIALACCPAY_SERVICE = "pay.qrcode.officialpay";
    /***银联行业码接口*/
    public static final String UNIONJSPAY_SERVICE = "pay.qrcode.unionjspay";

    public static final String NOTIFY_SERVICE = "trans.notify";

    public static final String STATICCODE_SERVICE = "pay.qrcode.staticcode";
    public static final String NFC_CREATE_SERVICE = "pay.qrcode.nfcCreate";
    public static final String NFC_UPDATE_SERVICE = "pay.qrcode.nfcUpdate";

    public static final String BACKWARD_QUERY_SERVICE = "out_qrcode_backward_query";

    /**
     * 渠道请求地址
     */
    public static final String CHN_QRCODE = "qrcode";
    public static final String CHN_BARCODE = "barCode";
    public static final String CHN_ORDER_QUERY = "order_query";
    public static final String CHN_JSAPI = "jsapi";
    public static final String CHN_QUERY_OPENID_USERID = "queryOpenIdUserId";
    public static final String CHN_BIND_AUTH = "wechatAuthorizationOuterService";
    public static final String CHN_REFUND = "refund";
    public static final String CHN_CANCEL = "cancel";
    public static final String CHN_INTERNAL_CANCEL = "definitely_cancel";

    public static final String CHN_NFC_TAG_CREATE = "nfcTagCreate";
    public static final String CHN_NFC_TAG_UPDATE = "nfcTagUpdate";
    /**
     * 业务大类
     */
    public static final String BUSI_TYPE_8001 = "8001";

    /**
     * 银联静态码和NFC使用1001大类
     */
    public static final String BUSI_TYPE_1001 = "1001";

    public static final String WXPAY = "wxpay";
    public static final String ALIPAY = "alipay";
    public static final String UNIONPAY = "unionpay";
    public static final String QQPAY = "qqpay";
    public static final String JLPAY = "jlpay";

    public static final String STATIC_CODE = "STATIC_CODE";
    public static final String NFC = "NFC";

    /**
     * 接入来源:0-下游外部接入,1-内部接入
     */
    public static final String OUTSIDE = "0";
    /**
     * 接入来源:0-下游外部接入,1-内部接入,静态码
     */
    public static final String INSIDE_STATIC_CODE = "1";
    /**
     * 是否分账:0-否，1-是, 默认不分账
     */
    public static final String NO_PROFITSHARING = "0";
    /**
     * 二维码类型 0-动态 1-静态
     */
    public static final String DYNAMIC_CODE = "0";

    /**
     * 是否是native封装
     * 0: 发往渠道生成真实二维码
     * 1: 调用三码合一服务,获取聚合码
     */
    public static final String IS_NATIVE_PACKAGE = "1";

    /**
     * 校验字段值为真
     */
    public static final String CHECK_FIELD_TRUE = "1";

    /**
     * 校验字段值为假
     */
    public static final String CHECK_FIELD_FALSE = "0";

    /**
     * 1: 已扫码
     */
    public static final String QRCODE_STATE_1 = "1";

    public static final String BACK_WARD_QUERY_FAIL_RET_CODE = "01";

    public static final String BACK_WARD_QUERY_UNKNOWN_RET_CODE = "02";

    public static final String INTERNAL_TRANS_CLOSE_RET_CODE = "Q2";

    public static final String INTERNAL_TRANS_CLOSE_RET_MSG = "外接码付超时关单";

    public static final String SIGN_NOT_ACCESS_RET_CODE = "QV";

    public static final String SIGN_NOT_ACCESS_RET_MSG = "验签不通过";

    /**
     * 渠道透传字段
     */
    /**
     * 银联二维码单品营销,渠道请求字段
     */
    public static final String UNIONPAY_ACQADDNDATA = "acqAddnData";
    /**
     * 微信单品营销,渠道请求字段
     */
    public static final String WXPAY_DETAIL = "detail";
    /**
     * 银联二维码成功交易前台通知url,渠道请求字段
     */
    public static final String UNIONPAY_FRONTURL = "frontUrl";
    /**
     * 银联二维码失败交易前台通知url,渠道请求字段
     */
    public static final String UNIONPAY_FRONTFAILURL = "frontFailUrl";
}
