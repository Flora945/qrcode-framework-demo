package com.jlpay.qrcode.external.business.services.protocol;

import com.jlpay.qrcode.external.db.model.OutQrPayOrder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 延迟撤销消息体
 * @author: lvlinyang
 * @date: 2019/11/13 11:00
 */
@Data
@Slf4j
public class DelayCancelMessage {

    private long delayMillis;

    private OutQrPayOrder outQrPayOrder;

    public DelayCancelMessage(long delayMillis) {
        this.delayMillis = delayMillis;
        log.info("延迟{}ms,执行内部撤销", delayMillis);
    }

}
