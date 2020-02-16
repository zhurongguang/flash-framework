package com.flash.framework.core.processor;

import com.flash.framework.core.support.processor.BizProcessorContext;
import lombok.Data;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午5:35
 */
@Data
public class DemoContext extends BizProcessorContext {

    private String demo1;

    private String demo2;

    private String thread;
}