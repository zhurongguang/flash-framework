package com.flash.framework.core.support.processor.graph.executor;

import com.flash.framework.core.support.processor.BizProcessor;
import com.google.common.base.Throwables;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 图任务节点
 *
 * @author zhurg
 * @date 2019/6/28 - 下午5:53
 */
@Slf4j
@ToString(exclude = {"processor", "latch", "latchWait", "taskExecutor"})
@EqualsAndHashCode(exclude = {"processor", "latch", "latchWait", "taskExecutor"})
public class TaskWrapper implements Comparator<TaskWrapper> {

    private BizProcessor processor;

    @Setter
    private CountDownLatch latch;

    @Setter
    private boolean latchWait;

    @Setter
    private GraphTaskExecutor taskExecutor;

    @Getter
    private String taskName;

    public TaskWrapper(BizProcessor processor) {
        this.processor = processor;
        this.taskName = processor.getProcessName();
    }

    public <C> C execute(C context) {
        try {

            //需要等待所有前置节点执行完
            if (Objects.nonNull(latch) && latchWait) {
                synchronized (latch) {
                    if (latch.getCount() > 0L) {
                        latch.wait();
                    }
                }
            }

            if (taskExecutor.running()) {

                this.processor.execute(context);

                //前置节点释放latch
                if (Objects.nonNull(latch) && !latchWait) {
                    synchronized (latch) {
                        latch.countDown();
                    }
                }

                //标识当前任务完成
                taskExecutor.finish(this);
            }

            return context;
        } catch (Exception e) {
            log.error("[Flash Framework] BizProcessor {} execute failed,cause:{}", getTaskName(), Throwables.getStackTraceAsString(e));
            taskExecutor.failed();
            if (Objects.nonNull(latch)) {
                synchronized (latch) {
                    if (latch.getCount() > 0L) {
                        //全部释放掉
                        for (long c = latch.getCount(); c >= 0; c--) {
                            latch.countDown();
                        }
                    }
                }
            }
            taskExecutor.failback(context);
            return null;
        }
    }

    public <C> void rollback(C context) {
        try {
            this.processor.failback(context);
        } catch (Exception e) {
            log.error("[Flash Framework] BizProcessor {} failback failed,cause:{}", getTaskName(), Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public int compare(TaskWrapper o1, TaskWrapper o2) {
        return o1.taskName.compareTo(o2.taskName);
    }
}