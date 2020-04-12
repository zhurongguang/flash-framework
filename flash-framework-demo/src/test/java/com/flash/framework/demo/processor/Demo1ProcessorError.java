package com.flash.framework.demo.processor;

import com.flash.framework.core.support.processor.BizProcessor;
import com.flash.framework.core.support.processor.Processor;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午5:31
 */
@Processor(name = "demo2PError")
public class Demo1ProcessorError implements BizProcessor<DemoContext> {

    @Override
    public void execute(DemoContext context) throws Exception {
        System.out.println("demo2 demo1PError exec");
        int r = 1 / 0;
    }

    @Override
    public void failback(DemoContext context) throws Exception {
        System.out.println("demo2 demo1PError failback");
    }
}
