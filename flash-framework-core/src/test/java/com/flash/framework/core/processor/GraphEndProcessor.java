package com.flash.framework.core.processor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.processor.BizProcessor;
import com.flash.framework.core.support.processor.BizProcessorContext;
import com.flash.framework.core.support.processor.Processor;

/**
 * @author
 * @date 2019/11/26 - 下午5:52
 */
@Processor(name = "graphEnd")
public class GraphEndProcessor implements BizProcessor {

    @Override
    public void execute(BizProcessorContext context) throws Exception {
        System.out.println("graphEnd exec ,context:" + JSON.toJSONString(context));
    }

    @Override
    public void failback(BizProcessorContext context) throws Exception {
        System.out.println("graphEnd failback ,context:" + JSON.toJSONString(context));
    }
}
