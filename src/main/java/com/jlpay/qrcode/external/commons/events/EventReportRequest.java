package com.jlpay.qrcode.external.commons.events;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jlpay.qrcode.external.commons.Constants;
import lombok.*;

import java.util.Date;

/**
 * 事件通知请求
 *
 * @author qihuaiyuan
 * @since 2021/02/06
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventReportRequest {

    /**
     * 等级
     */
    @JSONField(name = "alarm_level")
    private String level;
    /**
     * 触发时间
     */
    @JSONField(name = "alarm_time", format = Constants.STANDARD_DATE_FORMAT)
    private Date time;

    @JSONField(name = "service_model")
    private String model;

    /**
     * 触发服务
     */
    @JSONField(name = "exe_service")
    private String service;
    /**
     * 触发接口
     */
    @JSONField(name = "exe_interface")
    private String exeInterface;
    /**
     * 日志跟踪号
     */
    @JSONField(name = "service_log_id")
    private String logId;
    /**
     * 错误码
     */
    @JSONField(name = "err_code")
    private String errCode;
    /**
     * 错误信息
     */
    @JSONField(name = "err_msg")
    private String msg;
    /**
     * 服务所在的IP地址
     */
    @JSONField(name = "server_ip")
    private String ip;
}
