package com.flash.framework.demo.processor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.processor.BizProcessor;
import com.flash.framework.core.support.processor.Processor;

/**
 * @author
 * @date 2019/11/26 - 下午5:52
 */
@Processor(name = "graphStart")
public class GraphStartProcessor implements BizProcessor<DemoContext> {

    @Override
    public void execute(DemoContext context) throws Exception {
        System.out.println("graph start exec ,context:" + JSON.toJSONString(context));
    }

    @Override
    public void failback(DemoContext context) throws Exception {
        System.out.println("graph start failback ,context:" + JSON.toJSONString(context));
    }
}
