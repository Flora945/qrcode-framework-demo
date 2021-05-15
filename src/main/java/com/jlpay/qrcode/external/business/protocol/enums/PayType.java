package com.jlpay.qrcode.external.business.protocol.enums;

import com.jlpay.qrcode.external.db.model.OutQrPayOrder;

/**
 * 支付类型
 * @author zhaoyang2
 */
public enum PayType {

	/**微信*/
	wxpay("wxpay", "30", "1"),
	/**支付宝*/
	alipay("alipay", "31", "0"),
	/**银联*/
	unionpay("unionpay", "03", "2"),
	/**qq*/
	qqpay("qqpay", "29", "3"),
	/**未知*/
	unknow("", "", ""),
	/**嘉联聚合支付*/
	jlpay("jlpay", "", "");

	/**支付类型*/
	private String payType;
	/**计费类型*/
	private String feeCalcType;
	/**风控类型*/
	private String riskType;

	PayType(String payType, String feeCalcType, String riskType) {
		this.payType = payType;
		this.feeCalcType = feeCalcType;
		this.riskType = riskType;
	}

	/**
	 * 通过支付类型描述找到支付类型
	 * @param payType
	 * @return
	 */
	public static PayType findPayType(String payType) {
		for (PayType type : PayType.values()) {
			if (type.payType.equals(payType)) {
				return type;
			}
		}
		return unknow;
	}

	/**
	 * 通过计费类型找到支付类型
	 * @param feeCalcType
	 * @return
	 */
	public static PayType findPayTypeByFeeC(String feeCalcType) {
		for (PayType type : PayType.values()) {
			if (type.feeCalcType.equals(feeCalcType)) {
				return type;
			}
		}
		return unknow;
	}

	public static PayType resolvePayType(OutQrPayOrder payOrder) {
		if (TradeType.NATIVE_PACKAGE.getValue().equals(payOrder.getTradeType())) {
			return jlpay;
		} else {
			return findPayTypeByFeeC(payOrder.getFeeCalcType());
		}
	};

	public String getPayType() {
		return payType;
	}


	public String getFeeCalcType() {
		return feeCalcType;
	}


	public String getRiskType() {
		return riskType;
	}


}
