package com.flash.framework.demo.retry;

import com.flash.framework.core.support.retry.Retry;
import org.springframework.stereotype.Component;

/**
 * @author zhurg
 * @date 2019/12/2 - 下午2:27
 */
@Component
public class RetryService {

    @Retry(retry = 3, exception = ArithmeticException.class)
    public Boolean demo() {
        int r = 1 / 0;
        return Boolean.TRUE;
    }
}