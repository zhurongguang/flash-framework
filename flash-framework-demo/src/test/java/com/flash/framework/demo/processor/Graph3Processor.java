package com.flash.framework.demo.processor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.processor.BizProcessor;
import com.flash.framework.core.support.processor.Processor;

/**
 * @author
 * @date 2019/11/26 - 下午5:52
 */
@Processor(name = "graph3")
public class Graph3Processor implements BizProcessor<DemoContext> {

    @Override
    public void execute(DemoContext context) throws Exception {
        Thread.sleep(1000L);
        System.out.println("graph3 exec ,context:" + JSON.toJSONString(context));
        int r = 1 / 0;
    }

    @Override
    public void failback(DemoContext context) throws Exception {
        System.out.println("graph3 failback ,context:" + JSON.toJSONString(context));
    }
}
