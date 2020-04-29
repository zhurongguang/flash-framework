package com.flash.framework.core.support.extension.core;

import com.flash.framework.core.exception.extension.ExtensionException;
import com.flash.framework.core.support.extension.annotation.ExtensionProvider;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.apache.commons.collections.MapUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 扩展点注册中心
 *
 * @author zhurg
 * @date 2020/1/3 - 下午3:09
 */
@Component
public class ExtensionProviderRegistry implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * group - bizCode - beanName
     */
    private Table<String, String, String> extensions = HashBasedTable.create();

    /**
     * group : beanName
     */
    private Map<String, String> defaultExtensions = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(ExtensionProvider.class);
        if (MapUtils.isNotEmpty(providers)) {
            providers.forEach((name, provider) -> {
                ExtensionProvider ann;
                if (AopUtils.isAopProxy(provider) || AopUtils.isCglibProxy(provider)) {
                    ann = AnnotationUtils.findAnnotation(AopUtils.getTargetClass(provider), ExtensionProvider.class);
                } else {
                    ann = AnnotationUtils.findAnnotation(provider.getClass(), ExtensionProvider.class);
                }
                for (String bizCode : ann.bizCode()) {
                    if (extensions.contains(ann.group(), ann.bizCode())) {
                        throw new ExtensionException("[Flash Framework] ExtensionProvider group : " + ann.group() + " , bizCode : " + bizCode + " areadly exists");
                    }
                    extensions.put(ann.group(), bizCode, name);
                }
                if (ann.isDefault()) {
                    if (defaultExtensions.containsKey(ann.group())) {
                        throw new ExtensionException("[Flash Framework] ExtensionProvider group : " + ann.group() + " areadly exists default provider");
                    }
                    defaultExtensions.put(ann.group(), name);
                }
            });
        }
    }

    /**
     * 获取 ExtensionProvider
     *
     * @param group
     * @param bizCode
     * @return
     */
    public Object resolve(String group, String bizCode) {
        if (extensions.contains(group, bizCode)) {
            return applicationContext.getBean(extensions.get(group, bizCode));
        } else if (defaultExtensions.containsKey(group)) {
            return applicationContext.getBean(defaultExtensions.get(group));
        }
        throw new ExtensionException("[Flash Framework] can not fund ExtensionProvider for group : " + group + " , bizCode : " + bizCode);
    }
}