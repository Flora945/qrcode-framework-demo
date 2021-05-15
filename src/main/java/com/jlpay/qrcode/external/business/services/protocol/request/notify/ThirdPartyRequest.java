package com.jlpay.qrcode.external.business.services.protocol.request.notify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ThirdPartyRequest {
	@JSONField(name = "merch_no")
	private String merchNo;

	@JSONField(name = "org_code")
	private String orgCode;

	@JSONField(name = "out_trade_no")
	private String outTradeNo;

	@JSONField(name = "status")
	private String status;

	@JSONField(name = "trade_amount")
	private String tradeAmount;

	@JSONField(name = "order_no")
	private String orderNo;

	@JSONField(name = "trade_time", format = "yyyyMMddHHmmss")
	private String tradeTime;
	private String sign;

	@JSONField(name = "coupon_info")
	public String couponInfo;
	@JSONField(name = "finnal_amount")
	public String finnalAmount;
	@JSONField(name = "discount_amount")
	public String discountAmount;
	@JSONField(name = "coupon_name")
	public String couponName;
}
