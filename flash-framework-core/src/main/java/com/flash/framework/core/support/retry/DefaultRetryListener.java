package com.flash.framework.core.support.retry;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import lombok.extern.slf4j.Slf4j;

/**
 * guava retry 失败重试监听器默认实现
 *
 * @author zhurg
 * @date 2019/12/2 - 下午2:19
 */
@Slf4j
public class DefaultRetryListener implements RetryListener {

    private String method;

    public DefaultRetryListener(String method) {
        this.method = method;
    }

    @Override
    public <V> void onRetry(Attempt<V> attempt) {
        if (attempt.hasException()) {
            log.error("[Flash Framework] MethodRetry {} execute failed,cause:", method, attempt.getExceptionCause());
        }
    }
}
