package com.flash.framework.core.support.retry;

import com.flash.framework.commons.utils.AopUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * 重试切面
 *
 * @author zhurg
 */
@Slf4j
@Aspect
public class RetryAspect {

    @Autowired
    private RetryHelper retryHelper;

    /**
     * 方法重试切面
     *
     * @param joinPoint
     * @return
     */
    @AfterThrowing(value = "@annotation(com.flash.framework.core.support.retry.Retry)")
    public void retry(JoinPoint joinPoint) {
        Method method = AopUtils.getMethod(joinPoint);
        Retry retry = method.getAnnotation(Retry.class);
        try {
            retryHelper.retry(retry.retry() - 1, retry.interval(), retry.time(), retry.exception(),
                    new DefaultRetryListener(method.getDeclaringClass().getCanonicalName() + "." + method.getName()),
                    () -> method.invoke(joinPoint.getTarget(), joinPoint.getArgs()));
        } catch (Exception e) {
            log.error("[Flash Framework] MethodRetry {} execute retry failed,cause:", method.getDeclaringClass().getCanonicalName() + "." + method.getName(), e);
        }
    }
}