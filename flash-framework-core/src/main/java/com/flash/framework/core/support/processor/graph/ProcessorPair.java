package com.flash.framework.core.support.processor.graph;

import com.flash.framework.core.support.processor.BizProcessor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhurg
 * @date 2019/11/29 - 下午2:55
 */
@Data
@AllArgsConstructor
public class ProcessorPair {

    /**
     * 前驱
     */
    private BizProcessor predecessor;

    /**
     * 后继
     */
    private BizProcessor successor;
}