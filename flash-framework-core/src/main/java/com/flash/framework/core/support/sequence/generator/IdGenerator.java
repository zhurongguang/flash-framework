package com.flash.framework.core.support.sequence.generator;

/**
 * @author zhurg
 * @date 2021/6/25 - 下午1:48
 */
public interface IdGenerator {

    /**
     * 生成主键
     *
     * @param obj
     * @return
     */
    String generate(Object obj);
}