package com.flash.framework.core.support.extension.core;

import com.flash.framework.commons.utils.SpelParser;
import com.flash.framework.core.exception.extension.ExtensionException;
import com.flash.framework.core.support.extension.annotation.BizCodeParam;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author zhurg
 * @date 2020/1/29 - 上午11:14
 */
public class BaseInterceptorHandler {

    private static final String EL_PRIFIX = "bizCodeParam";

    protected final ExtensionProviderRegistry extensionProviderRegistry;

    protected static Map<String, Integer> bizCodeParamPositionCache = Maps.newConcurrentMap();

    protected static Map<String, String> bizCodeParamEl = Maps.newConcurrentMap();

    protected final String group;

    public BaseInterceptorHandler(ExtensionProviderRegistry extensionProviderRegistry, String group) {
        this.extensionProviderRegistry = extensionProviderRegistry;
        this.group = group;
    }

    /**
     * 解析bizCode
     *
     * @param method
     * @param args
     * @return
     */
    protected String resolveBizCode(Method method, Object[] args) {
        int index = bizCodePosition(method);
        String el = bizCodeParam(method);
        Object arg = args[index];
        String bizCode;
        if (StringUtils.isNotBlank(el)) {
            EvaluationContext context = new StandardEvaluationContext();
            context.setVariable(EL_PRIFIX, arg);
            StringBuffer sb = new StringBuffer(el);
            sb.insert(el.indexOf("#") + 1, EL_PRIFIX + ".");
            el = sb.toString();
            bizCode = SpelParser.EXPRESSION_PARSER.parseExpression(el).getValue(context, String.class);
        } else {
            bizCode = arg.toString();
        }
        return bizCode;
    }

    /**
     * 选择目标类
     *
     * @return
     */
    protected Object selectTarget(String bizCode) {
        return extensionProviderRegistry.resolve(group, bizCode);
    }

    /**
     * 获取@BizCodeParam 标识的参数所在的位置
     *
     * @param method 目标方法
     * @return 参数所在的位置
     */
    private int bizCodePosition(final Method method) {
        return bizCodeParamPositionCache.computeIfAbsent(method.toGenericString(), ignore -> {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if (annotation instanceof BizCodeParam) {
                        return i;
                    }
                }
            }
            throw new ExtensionException("[Flash Framework] can not fund @BizCodeParam annotated for method: " + method + " parameters");
        });
    }

    /**
     * 获取@BizCodeParam中的配置
     *
     * @param method
     * @return
     */
    private String bizCodeParam(final Method method) {
        return bizCodeParamEl.computeIfAbsent(method.toGenericString(), ignore -> {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if (annotation instanceof BizCodeParam) {
                        return ((BizCodeParam) annotation).el();
                    }
                }
            }
            throw new ExtensionException("[Flash Framework] can not fund @BizCodeParam annotated for method: " + method + " parameters");
        });
    }
}