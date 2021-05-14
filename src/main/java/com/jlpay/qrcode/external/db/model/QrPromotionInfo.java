package com.jlpay.qrcode.external.db.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 码付优惠信息表
 * @author: lvlinyang
 * @date: 2020/10/27 9:58
 */
@Data
public class QrPromotionInfo {

    /**订单号*/
    private String orderId;
    /**商品优惠标记*/
    private String goodsTag;
    /**商品信息*/
    private String goodsData;
    /**优惠券信息*/
    private String couponInfo;
    /**单品优惠信息*/
    private String dctGoodsInfo;
    /**创建时间*/
    private Date createTime;
    /**更新时间*/
    private Date updateTime;
}
