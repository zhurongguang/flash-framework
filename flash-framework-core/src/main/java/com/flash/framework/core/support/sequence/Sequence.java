package com.flash.framework.core.support.sequence;

import com.flash.framework.core.exception.sequence.SequenceException;
import com.flash.framework.core.support.sequence.generator.IDRule;

/**
 * @author zhurg
 * @date 2021/6/25 - 上午10:37
 */
public interface Sequence {

    /**
     * 获取一段序列
     *
     * @param name
     * @param idRule
     * @return
     * @throws SequenceException
     */
    SequenceRange nextRange(String name, IDRule idRule) throws SequenceException;

    /**
     * 获取序列下一个值
     *
     * @param name
     * @param idRule
     * @return
     * @throws SequenceException
     */
    long nextValue(String name, IDRule idRule) throws SequenceException;
}