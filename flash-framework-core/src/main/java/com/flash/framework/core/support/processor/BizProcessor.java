package com.flash.framework.core.support.processor;


import org.springframework.core.annotation.AnnotationUtils;

/**
 * 业务处理接口
 *
 * @author zhurg
 * @date 2018/11/7 - 下午12:59
 */
public interface BizProcessor<C extends BizProcessorContext> {

    String BIZ_PROCESSOR_CONTEXT = "context";

    /**
     * execute
     *
     * @param context 上下文
     */
    void execute(C context) throws Exception;

    /**
     * execute fallback
     *
     * @param context 上下文
     */
    void failback(C context) throws Exception;

    /**
     * 获取名称
     *
     * @return
     */
    default String getProcessName() {
        return getProcessor().name();
    }

    default Processor getProcessor() {
        return AnnotationUtils.findAnnotation(this.getClass(), Processor.class);
    }
}
