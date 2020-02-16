package com.flash.framework.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 统一响应实体
 *
 * @author zhurg
 * @date 2019/2/1 - 下午5:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("基础响应实体")
public class ResponseResult<B> implements Serializable {

    private static final long serialVersionUID = 8906245041478721853L;

    /**
     * 请求结果
     */
    @ApiModelProperty("请求结果")
    private boolean success;

    /**
     * 信息
     */
    @ApiModelProperty("请求信息")
    private String message;

    /**
     * 返回编码
     */
    @ApiModelProperty("响应编码")
    private String code;

    /**
     * 响应体
     */
    @ApiModelProperty("响应体")
    private B body;

    /**
     * 响应成功
     *
     * @param body
     * @param <B>
     * @return
     */
    public static <B> ResponseResult<B> success(B body) {
        return success(body, String.valueOf(HttpStatus.OK.value()));
    }

    /**
     * 响应成功
     *
     * @param body
     * @param <B>
     * @return
     */
    public static <B> ResponseResult<B> success(B body, String code) {
        return (ResponseResult<B>) ResponseResult.builder()
                .body(body)
                .success(true)
                .code(code)
                .build();
    }

    /**
     * 响应失败
     *
     * @param message
     * @param <B>
     * @return
     */
    public static <B> ResponseResult<B> fail(String message) {
        return (ResponseResult<B>) ResponseResult.builder()
                .success(false)
                .message(message)
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .build();
    }

    /**
     * 响应失败
     *
     * @param code
     * @param message
     * @param <B>
     * @return
     */
    public static <B> ResponseResult<B> fail(String code, String message) {
        return (ResponseResult<B>) ResponseResult.builder()
                .success(false)
                .message(message)
                .code(code)
                .build();
    }

    /**
     * 响应失败
     *
     * @param body
     * @param code
     * @param message
     * @param <B>
     * @return
     */
    public static <B> ResponseResult<B> fail(B body, String code, String message) {
        return (ResponseResult<B>) ResponseResult.builder()
                .body(body)
                .success(false)
                .message(message)
                .code(code)
                .build();
    }
}