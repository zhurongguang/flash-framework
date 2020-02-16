package com.flash.framework.web.support.version;

import com.google.common.collect.Sets;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * @author zhurg
 * @date 2019/4/4 - 下午5:19
 */
public class ApiRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private static final String VERSION = "{version}";

    private static final Set<Class<?>> MAPPINGS = Sets.newHashSet(RequestMapping.class, GetMapping.class, PostMapping.class, DeleteMapping.class, PutMapping.class);

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return createCondition(method);
    }


    private static RequestCondition<ApiVersionCondition> createCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.getAnnotation(method, ApiVersion.class);
        if (null != apiVersion) {
            Annotation[] annotations = BridgeMethodResolver.findBridgedMethod(method).getAnnotations();
            if (null != annotations && annotations.length > 0) {
                String methodRequestMapping = null;
                Optional<Annotation> annotation = Arrays.asList(annotations).stream().filter(a -> MAPPINGS.contains(a.annotationType())).findFirst();
                if (annotation.isPresent()) {
                    if (GetMapping.class.equals(annotation.get().annotationType())) {
                        methodRequestMapping = ((GetMapping) annotation.get()).value()[0];
                    } else if (PostMapping.class.equals(annotation.get().annotationType())) {
                        methodRequestMapping = ((PostMapping) annotation.get()).value()[0];
                    } else if (PutMapping.class.equals(annotation.get().annotationType())) {
                        methodRequestMapping = ((PutMapping) annotation.get()).value()[0];
                    } else if (DeleteMapping.class.equals(annotation.get().annotationType())) {
                        methodRequestMapping = ((DeleteMapping) annotation.get()).value()[0];
                    } else if (RequestMapping.class.equals(annotation.get().annotationType())) {
                        methodRequestMapping = ((RequestMapping) annotation.get()).value()[0];
                    }
                    if (!methodRequestMapping.contains(VERSION)) {
                        return null;
                    }
                    return new ApiVersionCondition(apiVersion.value());
                }
            }
        }
        return null;
    }
}