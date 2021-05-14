package com.jlpay.qrcode.external.db.model;

import lombok.Data;

import java.util.Date;

/**
  *
  * @author qihuaiyuan
  * @date 2019/12/2 16:43
  */ 
@Data
public class OutQrMerchantKey {
    /**
    * 机构号
    */
    private String orgCode;

    /**
    * 商户号
    */
    private String merchNo;

    /**
    * 系统私钥
    */
    private String sysPriKey;

    /**
    * 系统公钥
    */
    private String sysPubKey;

    /**
    * 商户公钥
    */
    private String merPubKey;

    /**
    * 状态，0初始，1正常，2停用，9删除
    */
    private String status;

    /**
    * 创建时间
    */
    private Date createDate;

    /**
    * md5 密钥
    */
    private String signKey;

    /**
    * 集团用户
    */
    private String groupNo;

    /**
    * 1-代理商,0-特约商户,2-设备厂商
    */
    private String accessType;

    /**
    * 机构名称
    */
    private String orgName;

    /**
    * 备注
    */
    private String remark;

    /**
    * 来源:1-外接,0-通行证.
    */
    private String orgSource;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 更新人
    */
    private String updateUser;

    /**
    * 创建人
    */
    private String createUser;

    /**
    * 交易类型：微信公众号1 微信扫码2 微信刷卡支付3 支付宝扫码4 支付宝刷卡5 支付宝服务窗6 银联行业码7 银联扫码8 银联刷卡9 微信app支付10 qq扫码11
 qq刷卡支付12
    */
    private String transType;

    /**
    * 是否检查商户 0否 1是
    */
    private String ifCheckMerch;

    /**
    * 系统版本 新外接码付配置1 老外接码付0 
    */
    private String sysVersion;

    /**
    * 是否检查终端 0否 1是
    */
    private String ifCheckTerm;
}