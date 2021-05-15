package com.jlpay.qrcode.external.config.properties;

import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * 订单延迟查询属性
 *
 * @author qihuaiyuan
 * @since 2020-09-22
 */
@Getter
@Setter
@Component
@ConfigurationProperties("jlpay.commons.system.timed-query")
public class TransTimedQueryProperties {

    /**
     * 提交查询任务路径
     */
    private static final String ADD_TASK_PATH = "/query";

    /**
     * 延迟等级
     */
    private static final int[] DELAY_LEVEL_SECONDS_INDEX = new int[]{1, 5, 10, 30, 60, 2 * 60, 3 * 60, 4 * 60, 6 * 60, 6 * 60, 7 * 60, 8 * 60, 9 * 60, 10 * 60, 20 * 60, 30 * 60, 3600, 2 * 3600};

    /**
     * 请求接口地址前缀
     */
    @NotBlank
    private String requestUrlPrefix;

    /**
     * 读取超时时间(ms)
     */
    private int timeout = 3000;

    /**
     * 初始延迟等级(默认: 6 #2min)
     */
    @Positive
    private int initialDelayLevel = 6;

    /**
     * 后续间隔延迟等级(默认: 7 #3min)
     */
    @Positive
    private int intervalDelayLevel = 7;

    /**
     * 最大查询次数
     */
    @Positive
    private int maxQueryTimes = 7;

    /**
     * 反查接口地址前缀
     */
    @NotBlank
    private String backwardQueryUrlPrefix;

    /**
     * 反查接口地址
     */
    @NotBlank
    private String backwardQueryUrl;

    /**
     * 添加定时查询任务接口地址
     */
    @NotBlank
    private String addQueryTaskUrl;

    /**
     * 最大查询时间
     */
    private long maxQueryMillis = -1;

    @PostConstruct
    public void validate() {
        ParamAssert.isTrue(initialDelayLevel <= 18 && initialDelayLevel >= 0, "延迟等级配置有误");
        ParamAssert.isTrue(intervalDelayLevel <= 18 && intervalDelayLevel >= 0, "延迟等级配置有误");
        addQueryTaskUrl = requestUrlPrefix + ADD_TASK_PATH;
        backwardQueryUrl = backwardQueryUrlPrefix + '/' + ApiConstants.BACKWARD_QUERY_SERVICE;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        maxQueryMillis = calculateMaxQueryMillis();
    }

    public long getMaxQueryMillis() {
        if (maxQueryMillis == -1) {
            maxQueryMillis = calculateMaxQueryMillis();
        }
        return maxQueryMillis;
    }

    /**
     * 计算最大查询时间
     *
     * @return 最大查询时间
     */
    private long calculateMaxQueryMillis() {
        return (getDelayMillis(initialDelayLevel) + (maxQueryTimes - 1) * getDelayMillis(intervalDelayLevel)) * 1000;
    }

    private long getDelayMillis(int delayLevel) {
        return DELAY_LEVEL_SECONDS_INDEX[delayLevel - 1];
    }

}
