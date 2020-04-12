package com.flash.framework.demo.batch;

import com.alibaba.fastjson.JSON;
import com.flash.framework.commons.paging.Paging;
import com.flash.framework.core.support.batch.BatchExecutor;
import com.flash.framework.core.support.batch.reader.PagingItemReader;
import com.flash.framework.core.support.batch.strategy.PagingConcurrentBatchProcessorStrategy;
import com.flash.framework.core.support.batch.strategy.ProcessConcurrentBatchProcessorStrategy;
import com.flash.framework.core.support.batch.strategy.SimpleBatchProcessorStrategy;
import com.flash.framework.demo.BaseTestService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午4:10
 */
public class DemoPagingBatchTest extends BaseTestService {

    /**
     * 构建线程池
     *
     * @return
     */
    private ThreadPoolTaskExecutor buildThreadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setKeepAliveSeconds(3);
        threadPoolTaskExecutor.setQueueCapacity(100);
        threadPoolTaskExecutor.setThreadNamePrefix("BatchExecutor-%d");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * 模拟数据存储
     */
    private static final List<Demo> datas = Lists.newArrayList(
            new Demo("1"),
            new Demo("2"),
            new Demo("3"),
            new Demo("4"),
            new Demo("5"),
            new Demo("6")
    );

    @Test
    public void batch() {
        BatchExecutor.builder()
                .itemReader(PagingItemReader.builder()
                        .context(new DemoPagingContext())
                        .pageSize(1)
                        .handler(((pageNo, pageSize, readerContext) -> {
                            int start = (int) ((pageNo - 1) * pageSize);
                            int end = start + (int) pageSize;
                            Paging<Demo> paging = new Paging<>();
                            paging.setPageSize(1);
                            paging.setTotal(datas.size());
                            paging.setRecords(datas.subList(start, end));
                            return paging;
                        }))
                        .build())
                .processorStrategy(new SimpleBatchProcessorStrategy())
                .build()
                .process((datas) ->
                        System.out.println("batch process : " + JSON.toJSONString(datas))
                );

    }

    @Test
    public void batch1() {
        BatchExecutor.builder()
                .itemReader(PagingItemReader.builder()
                        .context(new DemoPagingContext())
                        .pageSize(1)
                        .handler(((pageNo, pageSize, readerContext) -> {
                            int start = (int) ((pageNo - 1) * pageSize);
                            int end = start + (int) pageSize;
                            Paging<Demo> paging = new Paging<>();
                            paging.setPageSize(1);
                            paging.setTotal(datas.size());
                            paging.setRecords(datas.subList(start, end));
                            return paging;
                        }))
                        .build())
                .processorStrategy(PagingConcurrentBatchProcessorStrategy.builder()
                        .executor(buildThreadPoolExecutor()).build())
                .build()
                .process((datas) ->
                        System.out.println("batch process : " + JSON.toJSONString(datas))
                );
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void batch2() {
        BatchExecutor.builder()
                .itemReader(PagingItemReader.builder()
                        .context(new DemoPagingContext())
                        .pageSize(1)
                        .handler(((pageNo, pageSize, readerContext) -> {
                            int start = (int) ((pageNo - 1) * pageSize);
                            int end = start + (int) pageSize;
                            Paging<Demo> paging = new Paging<>();
                            paging.setPageSize(1);
                            paging.setTotal(datas.size());
                            paging.setRecords(datas.subList(start, end));
                            return paging;
                        }))
                        .build())
                .processorStrategy(ProcessConcurrentBatchProcessorStrategy.builder()
                        .executor(buildThreadPoolExecutor()).build())
                .build()
                .process((datas) ->
                        System.out.println("batch process : " + JSON.toJSONString(datas))
                );
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {

        }
    }
}