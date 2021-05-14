package com.jlpay.qrcode.external.business.dependency.protocol.request.timedquery;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 添加延迟查询任务请求
 *
 * @author qihuaiyuan
 * @since 2020-09-22
 */
@Getter
@Setter
public class AddTimedQueryTaskRequest {

    private String uri;

    private String content;

    @JSONField(name = "first_delay_level")
    private int firstDelayLevel;

    @JSONField(name = "interval_delay_level")
    private int intervalDelayLevel;

    @JSONField(name = "log_id")
    private String logId;

    private int times;

    @JSONField(name = "query_id")
    private String queryId;
}
