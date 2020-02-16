package com.flash.framework.core.support.extension.core;

import com.flash.framework.core.support.extension.annotation.Extension;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @author zhurg
 * @date 2020/1/3 - 下午4:05
 */
@Component
public class ExtensionBeanPostProcessor implements BeanPostProcessor {

    private final ExtensionProviderRegistry extensionProviderRegistry;

    @Autowired
    public ExtensionBeanPostProcessor(ExtensionProviderRegistry extensionProviderRegistry) {
        this.extensionProviderRegistry = extensionProviderRegistry;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(bean.getClass(), Extension.class);
        if (CollectionUtils.isNotEmpty(fields)) {
            Map<Class<?>, Object> proxyMap = Maps.newHashMap();
            fields.forEach(field -> {
                field.setAccessible(true);
                Extension ann = AnnotationUtils.findAnnotation(field, Extension.class);
                String group = ann.value();
                Class<?> type = field.getType();
                Object proxy;
                if (proxyMap.containsKey(type)) {
                    proxy = proxyMap.get(type);
                } else {
                    if (!type.isInterface()) {
                        //使用cglib代理
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(type);
                        enhancer.setCallback(new ExtensionInterceptorHandler(extensionProviderRegistry, group));
                        proxy = enhancer.create();
                    } else {
                        proxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(), new Class<?>[]{type},
                                new ExtensionInvokerHandler(extensionProviderRegistry, group));
                    }
                    proxyMap.put(type, proxy);
                }

                ReflectionUtils.setField(field, bean, proxy);
            });
        }
        return bean;
    }
}