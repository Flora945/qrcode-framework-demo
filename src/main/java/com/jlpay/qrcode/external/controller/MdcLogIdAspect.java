package com.jlpay.qrcode.external.controller;

import com.jlpay.qrcode.external.commons.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * MDC 日志切面，切入Controller的RequestMapping方法中，从http请求头读取logId放入MDC中
 *
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Slf4j
@Aspect
@Component
public class MdcLogIdAspect {

    @Around("execution(public void com.jlpay.qrcode.external.controller.ApiController.*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logIdEnhance(ProceedingJoinPoint pjp) {
        HttpServletRequest servletRequest = getServletRequest(pjp.getArgs());
        String logId = getLogId(servletRequest);
        MDC.put(Constants.MDC_LOG_ID_KEY, logId);
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            log.error("FATAL ERROR", throwable);
        } finally {
            MDC.remove(Constants.MDC_LOG_ID_KEY);
        }
        return null;
    }

    private HttpServletRequest getServletRequest(Object[] methodArgs) {
        for (Object arg : methodArgs) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        return null;
    }

    private String getLogId(HttpServletRequest request) {
        if (request != null) {
            String logId = request.getHeader(Constants.MDC_LOG_ID_KEY);
            if (StringUtils.isNotBlank(logId)) {
                return logId;
            }
        }
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
