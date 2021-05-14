package com.jlpay.qrcode.external.commons.exceptions.def;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
public class SystemException extends BaseException {

    private final String eventMessage;

    public static Builder builder() {
        return new Builder();
    }

    public SystemException(String exceptionCode, String message, String eventMessage) {
        super(exceptionCode, message);
        this.eventMessage = eventMessage;
    }

    public SystemException(String exceptionCode, String message, String eventMessage, Throwable cause) {
        super(exceptionCode, message, cause);
        this.eventMessage = eventMessage;
    }

    public SystemException(String exceptionCode, String message, Throwable cause) {
        super(exceptionCode, message, cause);
        this.eventMessage = cause.getLocalizedMessage();
    }

    public static class Builder {

        private String exceptionCode;

        private String retMessage;

        private String eventMessage;

        private Throwable cause;

        public SystemException build() {
            if (StringUtils.isBlank(exceptionCode)) {
                throw new IllegalArgumentException("exceptionCode is not provided");
            }
            if (StringUtils.isNotBlank(retMessage)) {
                throw new IllegalArgumentException("retMessage is not provided");
            }
            if (StringUtils.isBlank(eventMessage)) {
                eventMessage = retMessage;
            }
            if (cause != null) {
                return new SystemException(exceptionCode, retMessage, eventMessage, cause);
            }
            return new SystemException(exceptionCode, retMessage, eventMessage);
        }

        public Builder exceptionCode(String exceptionCode) {
            this.exceptionCode = exceptionCode;
            return this;
        }

        public Builder retMessage(String retMessage) {
            this.retMessage = retMessage;
            return this;
        }

        public Builder eventMessage(String eventMessage) {
            this.eventMessage = eventMessage;
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }
    }
}
