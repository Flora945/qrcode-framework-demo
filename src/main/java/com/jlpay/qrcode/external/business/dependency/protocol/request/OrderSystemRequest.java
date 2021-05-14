package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Description: 新订单系统请求
 * @author: lvlinyang
 * @date: 2021/3/19 15:30
 */
@Data
public class OrderSystemRequest {

    /**商户号*/
    @JSONField(name = "merch_no")
    private String merchNo;
    /**交易金额,以分为单位，不接受小数点*/
    private String amount;
    /**数据来源*/
    private String sources;
    /**订单状态(0-初始 1-待确认 2-成功 3-失败 默认: 0)*/
    private String status;
    /**业务大类*/
    @JSONField(name = "busi_type")
    private String busiType;
    /**交易类型,SALE(消费),VSALE(消费撤销),REFUND(退货),AUTH(预授权),VAUTH(预授权撤销),CAUTH(预授权完成),VCAUTH(预授权完成撤销),RCAUTH(预授权完成退款)*/
    @JSONField(name = "trans_type")
    private String transType;
    /**原交易订单号*/
    @JSONField(name = "ori_order_id")
    private String oriOrderId;
    /**外部订单号*/
    @JSONField(name = "out_order_id")
    private String outOrderId;
    /**支付凭证：提交支付所使用的tocken,如果是是用微信支付宝，银联二维码，则是使用的auth_code,如果是使用的是刷卡支付，则填写脱敏后的银行卡号*/
    @JSONField(name = "pay_token")
    private String payToken;
    /**支付方式，微信支付-wxpay;支付宝-alipay;银联二维码-unionpay;银行卡-bankpay*/
    @JSONField(name = "pay_type")
    private String payType;
    /**交易位置，可由交易IP，交易定位信息等数据组成。*/
    private String location;
    /**备注信息*/
    private String remark;
    /**订单有效时长。默认时长为5S.数字+时间单位。在这里S代表秒,M代表分钟,H代表小时，D代表天。订单延迟15秒，则填写15S，存在着D(天)\H(小时)\M(分钟)\S(秒)*/
    @JSONField(name = "valid_time")
    private String validTime;
    /**补偿查询地址，支持http和rpc协议。
     rpc协议,填写格式必须为rpc:///[zk注册节点]:[接口命令号]*/
    @JSONField(name = "query_url")
    private String queryUrl;
    /**订单关闭地址，支持http和rpc协议。rpc协议,填写格式必须为rpc:///[zk注册节点]:[接口命令号]*/
    @JSONField(name = "close_url")
    private String closeUrl;
    /**机身号*/
    @JSONField(name = "term_sn")
    private String termSn;
    /**打印商户名*/
    @JSONField(name = "print_merch_name")
    private String printMerchName;
    /**代理商ID*/
    @JSONField(name = "agent_id")
    private String agentId;
    /**地区码*/
    @JSONField(name = "area_code")
    private String areaCode;
    /**终端号*/
    @JSONField(name = "term_no")
    private String termNo;

}
