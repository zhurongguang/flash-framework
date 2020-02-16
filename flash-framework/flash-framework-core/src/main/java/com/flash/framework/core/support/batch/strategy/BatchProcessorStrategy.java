package com.flash.framework.core.support.batch.strategy;

import com.flash.framework.core.support.batch.reader.ItemReader;

import java.util.function.Consumer;

/**
 * 批处理策略
 *
 * @author zhurg
 * @date 2019/11/18 - 下午3:07
 */
public interface BatchProcessorStrategy<I extends ItemReader> {

    /**
     * 处理方法
     *
     * @param consumer
     * @param itemReader
     */
    void process(I itemReader, Consumer consumer) throws Exception;
}