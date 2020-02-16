package com.flash.framework.tools.validators;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 校验器
 *
 * @author zhurg
 * @date 2018/11/25 - 上午11:35
 */
@Data
public abstract class Validator<T> implements Comparable<Validator<?>> {

    /**
     * 排序字段
     */
    private int sort;

    /**
     * 校验器名称
     */
    private String validator;

    /**
     * 校验
     *
     * @param params
     * @return
     */
    public abstract boolean validate(T params);

    /**
     * 校验失败信息
     *
     * @return
     */
    public abstract String errorMsg();

    @Override
    public int compareTo(@NotNull Validator<?> o) {
        return this.getSort() - o.getSort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Validator<?> validator1 = (Validator<?>) o;
        return sort == validator1.sort &&
                Objects.equals(validator, validator1.validator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sort, validator);
    }
}