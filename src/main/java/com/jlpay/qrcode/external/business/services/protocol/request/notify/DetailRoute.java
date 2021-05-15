package com.jlpay.qrcode.external.business.services.protocol.request.notify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 回调消息分发参数
 * @author zhaoyang2
 */
@Data
public class DetailRoute {

	private String keyword;
	private String value;
	@JSONField(name = "url_info")
	private List<DetailUrl> urlInfo;
	
}
