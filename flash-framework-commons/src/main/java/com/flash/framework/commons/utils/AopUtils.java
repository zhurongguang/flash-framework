package com.flash.framework.commons.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Spring AOP 获取方法工具类
 *
 * @author zhurg
 * @date 2019/2/8 - 下午2:18
 */
public class AopUtils {

    public static final String CGLIB_SUFFIX = "CGLIB$CALLBACK_0";

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

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else if (org.springframework.aop.support.AopUtils.isCglibProxy(proxy)) {
            return getCglibProxyTargetObject(proxy);
        }
        return proxy;
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField(CGLIB_SUFFIX);
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }
}