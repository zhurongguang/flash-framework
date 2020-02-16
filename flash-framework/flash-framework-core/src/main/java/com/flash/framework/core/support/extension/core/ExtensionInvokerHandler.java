package com.flash.framework.core.support.extension.core;

import com.flash.framework.core.exception.extension.ExtensionException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 根据group选择对应的扩展实现
 *
 * @author zhurg
 * @date 2020/1/3 - 下午3:59
 */
public class ExtensionInvokerHandler extends BaseInterceptorHandler implements InvocationHandler {


    public ExtensionInvokerHandler(ExtensionProviderRegistry extensionProviderRegistry, String group) {
        super(extensionProviderRegistry, group);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String bizCode = resolveBizCode(method, args);
        Object target = selectTarget(bizCode);
        if (target != null) {
            return method.invoke(target, args);
        }
        throw new ExtensionException("[Flash Framework] can not fund ExtensionProvider for group : " + group + " , bizCode : " + bizCode);
    }
}
