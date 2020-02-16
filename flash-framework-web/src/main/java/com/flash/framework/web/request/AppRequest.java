package com.flash.framework.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhurg
 * @date 2019/4/8 - 上午10:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("基础请求实体")
public class AppRequest<B> implements Serializable {

    private static final long serialVersionUID = 5391303087802689757L;

    /**
     * 请求头
     */
    @ApiModelProperty("请求头")
    private RequestHeader header;

    /**
     * 请求体
     */
    @ApiModelProperty("请求体")
    private B body;
}