package com.flash.framework.tools.validators;

import cn.hutool.core.lang.Chain;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 校验器责任链
 *
 * @author zhurg
 * @date 2018/11/25 - 上午11:32
 */
public class ValidatorChain<T> implements Chain<Validator<T>, Boolean>, Serializable {

    private static final long serialVersionUID = -5466463349643173013L;

    /**
     * 校验器链
     */
    private List<Validator<T>> chain;

    public ValidatorChain(List<Validator<T>> chain) {
        this.chain = chain;
    }

    @Override
    public Boolean addChain(Validator<T> validator) {
        if (CollectionUtils.isEmpty(chain)) {
            this.chain = Lists.newArrayList();
        }
        this.chain.add(validator);
        Collections.sort(this.chain);
        return Boolean.TRUE;
    }


    /**
     * 执行校验器责任链
     *
     * @param params
     * @return
     */
    public boolean doValidatorChain(T params) throws Exception {
        chain.forEach(validator -> {
            if (!validator.validate(params)) {
                throw new RuntimeException(validator.errorMsg());
            }
        });
        return true;
    }

    @NotNull
    @Override
    public Iterator<Validator<T>> iterator() {
        return this.chain.iterator();
    }

    @Override
    public void forEach(Consumer<? super Validator<T>> action) {
        this.chain.forEach(action);
    }

    @Override
    public Spliterator<Validator<T>> spliterator() {
        return this.chain.spliterator();
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.chain);
    }
}