package com.flash.framework.core.support.batch.strategy;

import com.flash.framework.core.support.batch.reader.PagingItemReader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * 每页一处理，分页插叙顺序执行，对于每页的数据提交线程并发执行
 *
 * @author zhurg
 * @date 2019/11/18 - 下午3:57
 */
@Data
@Builder
public class ProcessConcurrentBatchProcessorStrategy implements BatchProcessorStrategy<PagingItemReader> {

    private ThreadPoolTaskExecutor executor;

    @Override
    public void process(PagingItemReader itemReader, Consumer consumer) throws Exception {
        List datas = itemReader.read();
        while (!CollectionUtils.isEmpty(datas)) {
            executor.submit(new ProcessConcurrentBatchTask(datas, consumer));
            datas = itemReader.read();
        }
    }

    @AllArgsConstructor
    private class ProcessConcurrentBatchTask<T> implements Callable<List<T>> {

        private List<T> datas;

        private Consumer consumer;

        @Override
        public List<T> call() throws Exception {
            if (!CollectionUtils.isEmpty(datas)) {
                consumer.accept(datas);
            }
            return datas;
        }
    }
}
