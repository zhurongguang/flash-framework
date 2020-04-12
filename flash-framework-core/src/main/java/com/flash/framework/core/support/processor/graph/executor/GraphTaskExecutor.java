package com.flash.framework.core.support.processor.graph.executor;

import com.flash.framework.core.support.processor.BizProcessor;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 图执行器
 *
 * @author zhurg
 * @date 2019/11/18 - 下午5:28
 */
@Slf4j
@EqualsAndHashCode(exclude = {"taskDag", "hasFailed", "mergeContextMap", "finished", "rollback", "executor", "executorLatch"})
public class GraphTaskExecutor {

    private MutableGraph<TaskWrapper> taskDag;

    private AtomicBoolean hasFailed = new AtomicBoolean(false);

    private Set<TaskWrapper> finished = Sets.newCopyOnWriteArraySet();

    private AtomicBoolean rollback = new AtomicBoolean(false);

    private final ThreadPoolExecutor executor;

    private Set<String> submitedTasks = Sets.newCopyOnWriteArraySet();

    private Map<TaskWrapper, CountDownLatch> taskMap = Maps.newConcurrentMap();

    private volatile CountDownLatch executorLatch;

    private String executorId;

    private GraphTaskExecutor(TaskExecutorBuilder builder) {
        taskDag = GraphBuilder.directed()
                .nodeOrder(ElementOrder.<TaskWrapper>insertion())
                .expectedNodeCount(builder.expectedTaskCount)
                .allowsSelfLoops(false)
                .build();
        if (CollectionUtils.isNotEmpty(builder.taskPairs)) {
            builder.taskPairs.forEach(pair -> taskDag.putEdge(pair.getForerunner(), pair.getSuccessor()));
        }
        this.executor = builder.executor;
        this.executorLatch = new CountDownLatch(builder.taskPairs.size());
        this.executorId = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static class TaskExecutorBuilder {

        private int expectedTaskCount;

        private List<TaskPair> taskPairs = Lists.newArrayListWithCapacity(expectedTaskCount);

        private ThreadPoolExecutor executor;

        private TaskExecutorBuilder(int expectedTaskCount) {
            this.expectedTaskCount = expectedTaskCount;
        }

        public static TaskExecutorBuilder builder(int expectedTaskCount) {
            return new TaskExecutorBuilder(expectedTaskCount);
        }

        public TaskExecutorBuilder putEdge(BizProcessor node1, BizProcessor node2) {
            taskPairs.add(new TaskPair(new TaskWrapper(node1), new TaskWrapper(node2)));
            return this;
        }

        public TaskExecutorBuilder putEdges(List<TaskPair> edges) {
            taskPairs.addAll(edges);
            return this;
        }

        public TaskExecutorBuilder executor(ThreadPoolExecutor executor) {
            this.executor = executor;
            return this;
        }

        public GraphTaskExecutor build() {
            return new GraphTaskExecutor(this);
        }
    }

    public <C> void execute(final C context) throws Exception {
        try {
            List<TaskWrapper> starts = Lists.newArrayList();

            List<TaskWrapper> tasks = Lists.newArrayList(taskDag.nodes());

            Traverser.forGraph(taskDag).breadthFirst(tasks.get(0)).forEach(taskWrapper -> {
                //起点
                if (taskDag.inDegree(taskWrapper) == 0) {
                    starts.add(taskWrapper);
                }

                taskWrapper.setTaskExecutor(this);

                //入度大于1，说明存在多个前驱，需要等待所有前驱都完成
                if (taskDag.inDegree(taskWrapper) > 1) {
                    CountDownLatch latch = new CountDownLatch(taskDag.inDegree(taskWrapper));
                    taskWrapper.setLatch(latch);
                    taskWrapper.setLatchWait(true);
                    //获取该节点所有前驱，设置latch,这里直接通过taskDag.predecessors循环设置不起作用，不清楚问什么（可能taskDag.predecessors拿到的只是一个拷贝）
                    for (TaskWrapper t : taskDag.predecessors(taskWrapper)) {
                        taskMap.put(t, latch);
                    }
                }
            });

            Traverser.forGraph(taskDag).breadthFirst(tasks.get(0)).forEach(task -> {
                if (taskMap.containsKey(task)) {
                    task.setLatch(taskMap.get(task));
                }
            });

            starts.forEach(taskWrapper -> {
                if (running()) {
                    submitTask(taskWrapper, context);
                }
            });

            executorLatch.await();
        } catch (Throwable e) {
            log.error("[Flash Framework] BizProcessor execute failed,cause:{}", Throwables.getStackTraceAsString(e));
            throw e;
        } finally {
            executor.shutdown();
        }
    }

    private <C> void executeGraph(TaskWrapper taskWrapper, C context) {
        Set<TaskWrapper> successors = taskDag.successors(taskWrapper);
        if (CollectionUtils.isNotEmpty(successors)) {
            successors.forEach(wrapper -> {
                if (running()) {
                    submitTask(wrapper, context);
                }
            });
        }
    }

    private <C> void submitTask(TaskWrapper task, C context) {
        synchronized (submitedTasks) {
            if (!submitedTasks.contains(task.getTaskName())) {
                submitedTasks.add(task.getTaskName());

                executor.submit(() -> {
                    C bizProcessorContext = task.execute(context);
                    if (Objects.isNull(bizProcessorContext)) {
                        return;
                    }
                    executeGraph(task, bizProcessorContext);
                });
            }
        }
    }

    /**
     * 任务完成
     *
     * @param task
     */
    public synchronized void finish(TaskWrapper task) {
        finished.add(task);
        executorLatch.countDown();
    }

    /**
     * 任务回滚
     */
    public synchronized <C> void failback(C context) {
        if (!running() && !rollback.getAndSet(true)) {
            finished.forEach(task -> task.rollback(context));
        }
    }

    /**
     * 任务失败
     */
    public void failed() {
        hasFailed.getAndSet(true);
        if (executorLatch.getCount() > 0L) {
            for (long i = executorLatch.getCount(); i >= 0; i--) {
                executorLatch.countDown();
            }
        }
    }

    /**
     * 任务状态
     *
     * @return
     */
    public boolean running() {
        return !hasFailed.get();
    }

    @Data
    @AllArgsConstructor
    public static class TaskPair {

        private TaskWrapper forerunner;

        private TaskWrapper successor;
    }
}