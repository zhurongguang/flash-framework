package com.flash.framework.core.retry;

import com.flash.framework.core.BaseTestService;
import com.flash.framework.core.support.retry.RetryHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhurg
 * @date 2019/12/2 - 下午2:28
 */
public class RetryTest extends BaseTestService {

    @Autowired
    private RetryService retryService;

    @Autowired
    private RetryHelper retryHelper;

    @Test
    public void testAnno() {
        retryService.demo();
    }

    @Test
    public void testByRetryHelper() throws Exception {
        retryHelper.retry(2, 1000, ArithmeticException.class, () -> {
            int r = 1 / 0;
            return Boolean.TRUE;
        });
    }
}