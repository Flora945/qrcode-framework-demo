package com.jlpay.qrcode.external.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class OutQrPayOrder {

    /**订单ID*/
    private String orderId;
    /**机构号*/
    private String orgCode;
    /**商户号*/
    private String merchNo;
    /**计费类型，30-微信；31-支付宝；03-银联二维码；29-qq*/
    private String feeCalcType;
    /**交易类型,0-条形码，1-扫码,2-公众号或服务窗*/
    private String tradeType;
    /**交易金额*/
    private Long amount;
    /**业务大类，8001*/
    private String busiType;
    /**业务小类*/
    private String busiSubType;
    /**原交易订单号*/
    private String oriOrderId;
    /**外部订单ID*/
    private String outOrderId;
    /**回调通知地址*/
    private String notifyUrl;
    /**授权码*/
    private String authCode;
    /**二维码地址*/
    private String qrcode;
    /**订单状态   0-新建订单，1-等待确认，2-成功，3-失败,4-已撤销/已冲正，5-已退款*/
    private String status;
    /**是否记账 0-未记账，1已记账*/
    private String accFlag;
    /**结算费率*/
    private String fee;
    /**添加时间*/
    private Date addTime;
    /**交易时间*/
    private Date transTime;
    /**更新时间*/
    private Date utime;
    /**商户订单日期*/
    private Date orderDate;
    /**渠道订单号*/
    private String chnOrderNo;
    /**会计日期*/
    private Date settleDate;
    /**微信appid*/
    private String appid;
    /**微信openid*/
    private String openid;
    /**微信公从号信息*/
    private String payInfo;
    /**附加手续费*/
    private Long addedFee;
    /**客户端IP*/
    private String clientIp;
    /**备注*/
    private String remark;
    /**原外部订单ID*/
    private String oriOutOrderId;
    /**终端号*/
    private String termNo;
    /**卡号*/
    private String cardNo;
    /**卡类型：0-借记卡，1-贷记卡*/
    private String cardType;
    /**卡标志：0-磁卡 1-接触IC卡 2-非接 3-非接云闪付*/
    private String cardFlag;
    /**内外卡标志：Y－非外卡，N－外卡*/
    private String isInternalCard;
    /**终端位置信息*/
    private String positonInfo;
    /**响应码*/
    private String retCode;
    /**响应码描述*/
    private String retMsg;
    /**渠道编号*/
    private String channelNo;
    /**来源*/
    private String source;
    /**机身号*/
    private String deviceSn;
    /**经度*/
    private String longitude;
    /**纬度*/
    private String latitude;
    /**优惠金额*/
    private Long discountAmount;
    /**地区码*/
    private String areaCode;
    /**交易简介*/
    private String subject;
    /**坐标系*/
    private String coordSystem;
    /**描述*/
    private String description;
    /**SETTLE_TYPE  入账标识    码付只会返回 1:全额入账  和2:以应结订单金额入账*/
    private String settleType;
    /**SETTLE_AMOUNT  应结订单金额*/
    private Long settleAmount;
    /**EXTER_DISCOUNT_AMOUNT  额外优惠金额*/
    private Long exterDiscountAmount;
    /**FINNAL_AMOUNT  实付金额*/
    private Long finnalAmount;
    /**DISCOUNT_NAME  优惠活动名称*/
    private String discountName;
    /**COUPON_INFO  优惠活动明细*/
    private String couponInfo;
    /**OP_USER_ID  操作员*/
    private String opUserId;
    /**OP_SHOP_ID  门店编号*/
    private String opShopId;
    /**OUT_DEVICE_SN  外部机身号*/
    private String outDeviceSn;
    /**QR_VERSION  码付版本号 新外接码付：V2.0*/
    private String qrVersion;
    /**PROFIT_SHARING  是否分账：0-否，1-是*/
    private String profitSharing;
    /**用户扫码状态<br/>
     * 1:已扫码
     * */
    private String qrcodeState;

    /**
     * 过期时间
     */
    private Date expireDate;
}
