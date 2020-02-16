package com.flash.framework.core.support.processor.chain;

import com.flash.framework.core.support.processor.graph.Edge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 任务池
 *
 * @author zhurg
 * @date 2019/6/24 - 下午5:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"processorChain", "processorGraph"})
public class BizProcessors {

    /**
     * 业务域
     */
    private String bizScope;

    /**
     * 操作
     */
    private String operation;

    /**
     * 链式处理
     */
    private Set<String> processorChain;

    /**
     * 图处理
     */
    private Set<Edge> processorGraph;
}