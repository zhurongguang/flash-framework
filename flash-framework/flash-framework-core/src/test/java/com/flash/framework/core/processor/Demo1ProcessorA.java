package com.flash.framework.core.processor;

import com.flash.framework.core.support.processor.BizProcessor;
import com.flash.framework.core.support.processor.BizProcessorContext;
import com.flash.framework.core.support.processor.Processor;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午5:31
 */
@Processor(name = "demo1PA")
public class Demo1ProcessorA implements BizProcessor<DemoContext> {

    @Override
    public void execute(DemoContext context) throws Exception {
        System.out.println("demo1 PA exec");
    }

    @Override
    public void failback(DemoContext context) throws Exception {
        System.out.println("demo1 PA failback");
    }
}
