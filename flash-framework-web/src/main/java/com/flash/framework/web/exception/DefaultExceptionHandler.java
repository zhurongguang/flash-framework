package com.flash.framework.web.exception;

import com.flash.framework.web.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 统一异常处理
 *
 * @author zhurg
 * @date 2019/2/1 - 下午5:41
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localResolver;

    /**
     * 处理其他任意异常
     *
     * @param exception 异常对象
     * @param request   http请求对象
     * @return 返回结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected ResponseResult onException(Exception exception, HttpServletRequest request) {
        log.error("未捕获的异常 : ", exception);
        Locale locale = localResolver.resolveLocale(request);
        String msg = messageSource.getMessage(exception.getMessage(), new String[]{}, "web.error", locale);
        return ResponseResult.builder()
                .success(false)
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message(msg)
                .build();
    }

    /**
     * Web 异常信息处理
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(WebException.class)
    @ResponseBody
    protected ResponseResult onWebException(Exception exception, HttpServletRequest request) {
        Locale locale = localResolver.resolveLocale(request);
        String msg = messageSource.getMessage(exception.getMessage(), new String[]{}, exception.getMessage(), locale);
        return ResponseResult.builder()
                .success(false)
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message(msg)
                .build();
    }
}