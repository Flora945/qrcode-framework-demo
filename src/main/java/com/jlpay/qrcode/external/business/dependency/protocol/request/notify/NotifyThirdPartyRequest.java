package com.jlpay.qrcode.external.business.dependency.protocol.request.notify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 回调格式
 * @author zhaoyang2
 */
@Data
public class NotifyThirdPartyRequest {

	private String body;
	/**
	 * 形如"2019-01-14 18:56:41"
	 */
	@JSONField(name = "notify_time")
	private String notifyTime;
	@JSONField(name = "notify_type")
	private String notifyType;
	@JSONField(name = "route_info")
	private List<DetailRoute> routeInfo;
	
}
