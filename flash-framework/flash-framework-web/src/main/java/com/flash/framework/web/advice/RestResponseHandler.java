package com.flash.framework.web.advice;

import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.flash.framework.web.response.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 将响应结果统一包装成 ResponseResult
 *
 * @author zhurg
 * @date 2019/2/1 - 下午5:50
 */
@ControllerAdvice
public class RestResponseHandler extends FastJsonViewResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(FastJsonContainer container, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        if (contentType.getType().equals(MediaType.APPLICATION_JSON.getType()) &&
                !returnType.getMethod().getReturnType().equals(ResponseEntity.class)) {
            Object data = container.getValue();
            if (null != data && !(data instanceof ResponseResult)) {
                container.setValue(ResponseResult.builder()
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .success(true)
                        .body(data)
                        .build());
            }
        }
        super.beforeBodyWriteInternal(container, contentType, returnType, request, response);
    }
}