package com.flash.framework.web.advice;

import com.flash.framework.web.response.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;

/**
 * 将响应结果统一包装成 ResponseResult
 *
 * @author zhurg
 * @date 2019/2/1 - 下午5:50
 */
@ControllerAdvice
public class RestResponseHandler extends JsonViewResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        if (contentType.getType().equals(MediaType.APPLICATION_JSON.getType()) &&
                !returnType.getMethod().getReturnType().equals(ResponseEntity.class)) {
            Object data = bodyContainer.getValue();
            if (null != data && !(data instanceof ResponseResult)) {
                bodyContainer.setValue(ResponseResult.builder()
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .success(true)
                        .body(data)
                        .build());
            }
        }
        super.beforeBodyWriteInternal(bodyContainer, contentType, returnType, request, response);
    }
}