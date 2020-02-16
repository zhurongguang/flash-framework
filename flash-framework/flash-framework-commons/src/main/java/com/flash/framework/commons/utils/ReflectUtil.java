package com.flash.framework.commons.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类，可以获取接口、类上的泛型
 *
 * @author zhurg
 * @date 2019/1/6 - 下午6:35
 */
@Slf4j
public class ReflectUtil {

    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceT(Object o, int index) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);

    }

    /**
     * 获取类上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        return checkType(type, index);
    }


    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("[Flash Framework] Expected a Class or ParameterizedType"
                    + ", but " + type + " is type of " + className);
        }
    }

    /**
     * 获取类或者接口上的第一个泛型
     *
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> Class<T> getGenericClass(Class clz) {
        return getGenericClass(clz, 0);
    }

    /**
     * 获取类或者接口上的第index个泛型
     *
     * @param clz
     * @param index
     * @param <T>
     * @return
     */
    public static <T> Class<T> getGenericClass(Class clz, int index) {
        Type genericSuperclass = clz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length > index && types[index] instanceof Class) {
                return (Class) types[index];
            }
        } else if (!clz.getSuperclass().isAssignableFrom(Object.class)) {
            return getGenericClass(clz.getSuperclass(), index);
        }
        return (Class) Object.class;
    }
}