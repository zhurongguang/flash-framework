package com.flash.framework.core.support.processor.chain.executor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.processor.BizProcessor;
import com.flash.framework.core.support.processor.BizProcessorContext;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.MVEL;

import java.util.Collections;
import java.util.List;

/**
 * 链式任务处理
 *
 * @author zhurg
 * @date 2019/11/27 - 上午11:19
 */
@Slf4j
@Data
@Builder
public class ChainTaskExecutor {

    private List<BizProcessor> processors;

    public void execute(BizProcessorContext context) {
        List<BizProcessor> finished = Lists.newArrayListWithCapacity(processors.size());
        processors.forEach(processor -> {
            try {
                if (StringUtils.isBlank(processor.getProcessor().condition())) {
                    processor.execute(context);
                    finished.add(processor);
                } else {
                    if (MVEL.evalToBoolean(String.format("%s.%s", BizProcessor.BIZ_PROCESSOR_CONTEXT, processor.getProcessor().condition()),
                            ImmutableMap.of(BizProcessor.BIZ_PROCESSOR_CONTEXT, context))) {
                        processor.execute(context);
                        finished.add(processor);
                    } else {
                        log.info("[Flash Framework] BizProcessor {} was skiped", processor.getProcessName());
                    }
                }
            } catch (Exception e) {
                log.error("[Flash Framework] BizProcessor {} ,BizProcessorContext {} execute failed , cause:", processor.getProcessName(),
                        JSON.toJSONString(context), e);
                if (CollectionUtils.isNotEmpty(finished)) {
                    Collections.reverse(finished);
                    finished.forEach(it -> {
                        try {
                            it.failback(context);
                        } catch (Exception e1) {
                            log.error("[Flash Framework] BizProcessor {} , BizProcessorContext {} failback failed , cause:", processor.getProcessName(),
                                    JSON.toJSONString(context), e1);
                        }
                    });
                }
            }
        });
    }
}