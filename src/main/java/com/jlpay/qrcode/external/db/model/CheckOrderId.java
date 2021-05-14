package com.jlpay.qrcode.external.db.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 外接码付商户订单号唯一判断表
 * @author: lvlinyang
 * @date: 2019/11/8 20:30
 */
@Data
public class CheckOrderId {

    /**机构号+商户订单号*/
    private String orgAndOrderId;
    /**创建时间*/
    private Date createTime;
}
