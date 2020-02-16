package com.flash.framework.core.support.extension.core;

import com.flash.framework.core.exception.extension.ExtensionException;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zhurg
 * @date 2020/1/29 - 上午11:21
 */
public class ExtensionInterceptorHandler extends BaseInterceptorHandler implements MethodInterceptor {

    public ExtensionInterceptorHandler(ExtensionProviderRegistry extensionProviderRegistry, String group) {
        super(extensionProviderRegistry, group);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String bizCode = resolveBizCode(method, args);
        Object target = selectTarget(bizCode);
        if (target != null) {
            return proxy.invoke(target, args);
        }
        throw new ExtensionException("[Flash Framework] can not fund ExtensionProvider for group : " + group + " , bizCode : " + bizCode);
    }
}
