package com.flash.framework.commons.utils;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * Spel表达式工具类，用于解析自定义SPEL表达式
 *
 * @author zhurg
 * @date 2019/1/6 - 下午6:19
 */
public class SpelParser {

    public static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    public static final LocalVariableTableParameterNameDiscoverer LOCAL_VARIABLE_TABLE_PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 解析方法上的spel表达式获取值
     *
     * @param key
     * @param method
     * @param arguments
     * @return
     */
    public static String getValue(String key, Method method, Object[] arguments) {
        try {
            String[] paramNames = LOCAL_VARIABLE_TABLE_PARAMETER_NAME_DISCOVERER.getParameterNames(method);
            if (null == paramNames || paramNames.length <= 0) {
                return null;
            }
            Expression expression = EXPRESSION_PARSER.parseExpression(key);
            EvaluationContext context = new StandardEvaluationContext();
            int length = paramNames.length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    context.setVariable(paramNames[i], arguments[i]);
                }
            }
            return expression.getValue(context, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}