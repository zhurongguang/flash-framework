package com.flash.framework.core.support.processor;

import com.alibaba.fastjson.JSONArray;
import com.flash.framework.core.exception.processor.BizProcessorException;
import com.flash.framework.core.support.processor.chain.BizProcessors;
import com.flash.framework.core.support.processor.chain.executor.ChainTaskExecutor;
import com.flash.framework.core.support.processor.graph.ProcessorPair;
import com.flash.framework.core.support.processor.graph.executor.GraphTaskExecutor;
import com.flash.framework.core.support.processor.graph.executor.TaskWrapper;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhurg
 * @date 2018/11/7 - 下午1:06
 */
@Slf4j
public class BizProcessorManager implements ApplicationContextAware {

    @Value("${flash.framework.bizProcessors.path:PROCESSORS}")
    private String processorsPath;

    @Value("${flash.framework.bizProcessors.prefix:*Processors}")
    private String processorsPrefix;

    @Value("${flash.framework.bizProcessors.suffix:json}")
    private String processorsSuffix;

    private ApplicationContext applicationContext;

    /**
     * 链式调用
     */
    private ImmutableListMultimap<String, BizProcessor> chainProcessors;

    /**
     * 图处理
     */
    private ImmutableListMultimap<String, ProcessorPair> graphProcessors;


    @PostConstruct
    public void init() {
        ImmutableListMultimap.Builder<String, BizProcessor> chainBuilder = ImmutableListMultimap.builder();
        ImmutableListMultimap.Builder<String, ProcessorPair> graphBuilder = ImmutableListMultimap.builder();


        //加载配置文件
        loadBizProcessorFromProperties(chainBuilder, graphBuilder);

        chainProcessors = chainBuilder.build();
        graphProcessors = graphBuilder.build();
    }

    /**
     * 执行指定业务
     *
     * @param bizScope
     * @param operation
     * @param context
     */
    public <C> void doProcessors(String bizScope, String operation, C context) throws Exception {
        if (chainProcessors.containsKey(resolveBiz(bizScope, operation))) {
            List<BizProcessor> processors = chainProcessors.get(resolveBiz(bizScope, operation));
            ChainTaskExecutor.builder()
                    .processors(processors)
                    .build()
                    .execute(context);
        } else if (graphProcessors.containsKey(resolveBiz(bizScope, operation))) {
            List<ProcessorPair> taskPairs = graphProcessors.get(resolveBiz(bizScope, operation));
            Set<String> tasks = Sets.newHashSet();
            taskPairs.forEach(pair -> {
                tasks.add(pair.getPredecessor().getProcessName());
                tasks.add(pair.getSuccessor().getProcessName());
            });

            GraphTaskExecutor.TaskExecutorBuilder
                    .builder(tasks.size())
                    .executor(new ThreadPoolExecutor(1, tasks.size(),
                            1L, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(1024), new ThreadFactoryBuilder().setNameFormat("GraphBizProcessor-Thread-%d").build()))
                    .putEdges(taskPairs.stream()
                            .map(pair ->
                                    new GraphTaskExecutor.TaskPair(new TaskWrapper(pair.getPredecessor()), new TaskWrapper(pair.getSuccessor())))
                            .collect(Collectors.toList()))
                    .build()
                    .execute(context);
        }
    }

    /**
     * 加载配置文件数据
     *
     * @param chainBuilder
     * @param graphBuilder
     */
    private void loadBizProcessorFromProperties(ImmutableListMultimap.Builder<String, BizProcessor> chainBuilder, ImmutableListMultimap.Builder<String, ProcessorPair> graphBuilder) {
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + MessageFormat.format("/{0}/{1}.{2}", processorsPath, processorsPrefix, processorsSuffix);
            ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
            Resource[] source = resourceLoader.getResources(pattern);
            Set<BizProcessors> set = Sets.newHashSet();
            for (Resource resource : source) {
                if (resource.isReadable()) {
                    String json = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
                    if (StringUtils.isNotBlank(json)) {
                        set.addAll(JSONArray.parseArray(json).toJavaList(BizProcessors.class));
                    }
                }
            }
            if (!CollectionUtils.isEmpty(set)) {
                Map<String, BizProcessor> springProcessorBeans = applicationContext.getBeansOfType(BizProcessor.class);
                Map<String, BizProcessor> processorMap = Maps.newHashMap();
                if (MapUtils.isNotEmpty(springProcessorBeans)) {
                    springProcessorBeans.values().forEach(bean -> {
                        Processor anno = AnnotationUtils.findAnnotation(bean.getClass(), Processor.class);
                        if (Objects.isNull(anno)) {
                            throw new BizProcessorException(MessageFormat.format("[Flash Framework] BizProcessor {0} is not annotated by @Processor", bean.getClass()));
                        }
                        processorMap.put(anno.name(), bean);
                    });
                }
                set.stream().filter(data -> !StringUtils.isAllBlank(data.getBizScope(), data.getOperation()) &&
                        (Objects.nonNull(data.getProcessorChain()) || Objects.nonNull(data.getProcessorGraph())))
                        .forEach(data -> {
                            if (CollectionUtils.isNotEmpty(data.getProcessorChain())) {
                                data.getProcessorChain().forEach(processor -> {
                                    if (!processorMap.containsKey(processor)) {
                                        throw new BizProcessorException(MessageFormat.format("[Flash Framework] BizProcessor {0} not fund", processor));
                                    }
                                    chainBuilder.put(resolveBiz(data.getBizScope(), data.getOperation()), processorMap.get(processor));
                                });
                            } else if (CollectionUtils.isNotEmpty(data.getProcessorGraph())) {
                                data.getProcessorGraph().forEach(graph -> {
                                    if (!processorMap.containsKey(graph.getPredecessor())) {
                                        throw new BizProcessorException(MessageFormat.format("[Flash Framework] BizProcessor {0} not fund", graph.getPredecessor()));
                                    }
                                    if (!processorMap.containsKey(graph.getSuccessor())) {
                                        throw new BizProcessorException(MessageFormat.format("[Flash Framework] BizProcessor {0} not fund", graph.getSuccessor()));
                                    }

                                    graphBuilder.put(resolveBiz(data.getBizScope(), data.getOperation()), new ProcessorPair(processorMap.get(graph.getPredecessor()), processorMap.get(graph.getSuccessor())));
                                });
                            }
                        });
            }
        } catch (Exception e) {
            log.error("[Flash Framework] load BizProcessor from properties file failed,cause:{}", Throwables.getStackTraceAsString(e));
        }
    }


    protected String resolveBiz(String bizScope, String operation) {
        return String.format("%s:%s", bizScope, operation);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
