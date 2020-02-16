package com.flash.framework.core.processor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.processor.BizProcessor;
import com.flash.framework.core.support.processor.Processor;

/**
 * @author
 * @date 2019/11/26 - 下午5:52
 */
@Processor(name = "graph2")
public class Graph2Processor implements BizProcessor<DemoContext> {

    @Override
    public void execute(DemoContext context) throws Exception {
        Thread.sleep(3000L);
        System.out.println("graph2 exec ,context:" + JSON.toJSONString(context));
        context.setDemo2("graph2");
    }

    @Override
    public void failback(DemoContext context) throws Exception {
        System.out.println("graph2 failback ,context:" + JSON.toJSONString(context));
    }
}
