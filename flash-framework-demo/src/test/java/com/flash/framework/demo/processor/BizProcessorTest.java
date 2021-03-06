package com.flash.framework.demo.processor;

import com.flash.framework.core.support.processor.BizProcessorManager;
import com.flash.framework.demo.BaseTestService;
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
    public void testChain() throws Exception {
        bizProcessorManager.doProcessors("demo", "demo1", new DemoContext());
    }

    @Test
    public void testChainError() throws Exception {
        bizProcessorManager.doProcessors("demo", "demo2", new DemoContext());
    }

    @Test
    public void testGraph() throws Exception {
        bizProcessorManager.doProcessors("demo", "demo3", new DemoContext());
        sleep();
    }

    @Test
    public void testGraphError() throws Exception {
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
                try {
                    bizProcessorManager.doProcessors("demo", "demo3", context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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