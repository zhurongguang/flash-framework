package com.flash.framework.core.support.batch;

import com.flash.framework.core.support.batch.reader.ItemReader;
import com.flash.framework.core.support.batch.strategy.BatchProcessorStrategy;
import com.google.common.base.Throwables;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * 批处理器
 *
 * @author zhurg
 * @date 2019/11/18 - 上午11:44
 */
@Slf4j
@Data
@Builder
public class BatchExecutor<T> {

    private ItemReader<T> itemReader;

    private BatchProcessorStrategy processorStrategy;

    public BatchExecutor<T> setItemReader(ItemReader<T> itemReader) {
        this.itemReader = itemReader;
        return this;
    }

    public BatchExecutor<T> setProcessorStrategy(BatchProcessorStrategy processorStrategy) {
        this.processorStrategy = processorStrategy;
        return this;
    }

    public void process(Consumer consumer) {
        try {
            processorStrategy.process(itemReader, consumer);
        } catch (Exception e) {
            log.error("[Flash Framework] BatchExecutor process failed,cause:{}", Throwables.getStackTraceAsString(e));
        }
    }
}