package com.jlpay.qrcode.external.db.model;

import lombok.Data;

import java.util.Date;

/**
 * @author qihuaiyuan
 * @since 2019/9/3 16:31
 */
@Data
public class OutQrStaticQrcode {

    /**交易订单ID*/
    private String orderId;
    /**渠道订单号*/
    private String channelOrderNo;
    /**接入商户号*/
    private String merchNo;
    /**银联商户号*/
    private String channelMerchNo;
    /**终端号*/
    private String termNo;
    /**交易金额*/
    private Long amount;
    /**交易时间*/
    private Date orderTime;
    /**二维码/条形码*/
    private String qrcodeUrl;
    /**订单标题*/
    private String subject;
    /**订单描述*/
    private String subjectDetail;
    /**订单回调地址*/
    private String notifyUrl;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date updateTime;
    /**商户名称*/
    private String merName;
    /**代理商号*/
    private String channelAgent;
    /**1=银联*/
    private String qrcodeType;
    /**0=失败，1=成功*/
    private String status;
    /**机构号*/
    private String orgCode;
    /**机身号*/
    private String deviceSn;
    /**标签数据*/
    private String nfcTag;
    /**芯片ID*/
    private String nfcId;
}