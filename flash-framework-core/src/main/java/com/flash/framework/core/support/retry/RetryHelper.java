package com.flash.framework.core.support.retry;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 重试辅助类
 *
 * @author zhurg
 * @date 2019/12/2 - 下午1:47
 */
@Slf4j
@Component
public class RetryHelper {

    public <R> R retry(int retryTimes, long retryInterval, Class<? extends Throwable> retryException, Callable<R> callable) throws Exception {
        return retry(retryTimes, retryInterval, TimeUnit.MILLISECONDS, retryException, new RetryListener() {
            @Override
            public <V> void onRetry(Attempt<V> attempt) {
                if (attempt.hasException()) {
                    log.error("[Flash Framework] RetryMethod method execute failed,cause:", attempt.getExceptionCause());
                }
            }
        }, callable);
    }


    /**
     * 固定时延、次数重试
     *
     * @param retryTimes            重试次数
     * @param retryInterval         重试间隔时间
     * @param retryIntervalTimeUnit
     * @param retryException
     * @param retryListener
     * @param callable
     * @param <R>
     * @return
     * @throws Exception
     */
    public <R> R retry(int retryTimes, long retryInterval, TimeUnit retryIntervalTimeUnit, Class<? extends Throwable> retryException, RetryListener retryListener, Callable<R> callable) throws Exception {
        return retry(retryTimes, retryException, WaitStrategies.fixedWait(retryInterval, retryIntervalTimeUnit), retryListener, callable);
    }

    /**
     * 固定次数重试
     *
     * @param retryTimes     重试次数
     * @param retryException
     * @param waitStrategy
     * @param retryListener
     * @param callable
     * @param <R>
     * @return
     * @throws Exception
     */
    public <R> R retry(int retryTimes, Class<? extends Throwable> retryException, WaitStrategy waitStrategy, RetryListener retryListener, Callable<R> callable) throws Exception {
        return retry(retryException, waitStrategy, StopStrategies.stopAfterAttempt(retryTimes), retryListener, callable);
    }

    /**
     * 构建重试方法
     *
     * @param retryException 触发重试的异常
     * @param waitStrategy   重试等待策略
     * @param stopStrategy   重试停止策略
     * @param retryListener  重试监听器
     * @param callable       执行方法
     * @param <R>
     * @return
     * @throws Exception
     */
    public <R> R retry(Class<? extends Throwable> retryException, WaitStrategy waitStrategy, StopStrategy stopStrategy, RetryListener retryListener, Callable<R> callable) throws Exception {
        return RetryerBuilder.<R>newBuilder()
                .retryIfRuntimeException()
                .retryIfException()
                .retryIfExceptionOfType(retryException)
                .withWaitStrategy(waitStrategy)
                .withStopStrategy(stopStrategy)
                .withRetryListener(retryListener)
                .build()
                .call(callable);
    }
}