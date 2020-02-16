package com.flash.framework.commons.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * Spring AOP 获取方法工具类
 *
 * @author zhurg
 * @date 2019/2/8 - 下午2:18
 */
public class AopUtils {

    /**
     * 获取方法
     *
     * @param joinPoint
     * @returngetMethod
     */
    public static Method getMethod(JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(joinPoint.getSignature().getName())) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }

    /**
     * 获取方法
     *
     * @param proceedingJoinPoint
     * @return
     */
    public static Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
        Method[] methods = proceedingJoinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(proceedingJoinPoint.getSignature().getName())) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }
}