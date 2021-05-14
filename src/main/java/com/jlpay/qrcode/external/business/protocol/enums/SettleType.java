package com.jlpay.qrcode.external.business.protocol.enums;

/**
 * 结算类型
 * @author zhaoyang2
 */
public enum SettleType {

	/**
	 * 全额入账
	 */
	FULLENTRY("全额入账", "1"),
	/**
	 * 应结订单金额入账
	 */
	ORDERAMTENTRY("应结订单金额入账", "2");
	
	private String desc;
	private String value;

	SettleType(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
