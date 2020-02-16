package com.flash.framework.core.support.batch.strategy;

import com.flash.framework.commons.paging.Paging;
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
 * 分页并发进行处理，每一页分配一个线程去执行，每个线程去执行完整的查询及处理逻辑
 *
 * @author zhurg
 * @date 2019/11/18 - 下午3:23
 */
@Data
@Builder
public class PagingConcurrentBatchProcessorStrategy implements BatchProcessorStrategy<PagingItemReader> {

    private ThreadPoolTaskExecutor executor;

    @Override
    public void process(PagingItemReader itemReader, Consumer consumer) throws Exception {
        List datas = itemReader.read();
        if (CollectionUtils.isEmpty(datas)) {
            return;
        }
        long pageNo = 1;
        long pages = itemReader.getHelper().getPages();
        while (pageNo <= pages) {
            executor.submit(new PagingConcurrentBatchTask(pageNo, itemReader.getHelper().getPageSize(), itemReader.getHelper().getContext(), itemReader, consumer));
            pageNo++;
        }
    }

    @AllArgsConstructor
    private class PagingConcurrentBatchTask<T> implements Callable<Paging<T>> {

        private long pageNo;

        private long pageSize;

        private Object readerContext;

        private PagingItemReader<T, Object> itemReader;

        private Consumer consumer;

        @Override
        public Paging<T> call() throws Exception {
            Paging<T> paging = itemReader.getHelper().getPagingHandler().doRead(pageNo, pageSize, readerContext);
            if (!paging.isEmpty()) {
                consumer.accept(paging.getRecords());
            }
            return paging;
        }
    }
}
