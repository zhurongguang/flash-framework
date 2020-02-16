package com.flash.framework.core.processor;

import com.flash.framework.core.BaseTestService;
import com.flash.framework.core.support.processor.BizProcessorManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午5:34
 */
public class BizProcessorTest extends BaseTestService {

    @Autowired
    private BizProcessorManager bizProcessorManager;

    @Test
    public void testChain() {
        bizProcessorManager.doProcessors("demo", "demo1", new DemoContext());
    }

    @Test
    public void testChainError() {
        bizProcessorManager.doProcessors("demo", "demo2", new DemoContext());
    }

    @Test
    public void testGraph() {
        bizProcessorManager.doProcessors("demo", "demo3", new DemoContext());
        sleep();
    }

    @Test
    public void testGraphError() {
        bizProcessorManager.doProcessors("demo", "demo4", new DemoContext());
        //sleep(10000);
    }

    @Test
    public void testThread() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            executorService.execute(() -> {
                DemoContext context = new DemoContext();
                context.setThread("thread" + finalI);
                bizProcessorManager.doProcessors("demo", "demo3", context);
            });
        }
        sleep(40000);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep() {
        sleep(4000);
    }
}