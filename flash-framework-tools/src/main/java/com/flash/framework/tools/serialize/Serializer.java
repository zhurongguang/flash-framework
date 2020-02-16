package com.flash.framework.tools.serialize;


import org.jetbrains.annotations.Nullable;

/**
 * @author zhurg
 * @date 2019/2/12 - 下午3:27
 */
public interface Serializer<T> {

    /**
     * 序列化
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @Nullable
    byte[] serialize(@Nullable T obj) throws Exception;

    /**
     * 反序列化
     *
     * @param array
     * @return
     * @throws Exception
     */
    @Nullable
    T deserialize(@Nullable byte[] array) throws Exception;

    /**
     * 反序列化
     *
     * @param array
     * @param clazz
     * @return
     * @throws Exception
     */
    @Nullable
    T deserialize(@Nullable byte[] array, Class<T> clazz) throws Exception;
}