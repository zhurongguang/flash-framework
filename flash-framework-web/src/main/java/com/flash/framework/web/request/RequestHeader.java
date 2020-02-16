package com.flash.framework.web.request;

import com.flash.framework.web.app.DeviceInfo;
import com.flash.framework.web.app.VersionInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhurg
 */
@Data
@ApiModel("请求头实体")
public class RequestHeader implements Serializable {

    private static final long serialVersionUID = -9075651626867472615L;
    @ApiModelProperty("请求方法类型")
    private String functionType;
    @ApiModelProperty("设备信息")
    private DeviceInfo deviceInfo;
    @ApiModelProperty("版本信息")
    private VersionInfo versionInfo;
    @ApiModelProperty("请求时间")
    private String requestTime;
    @ApiModelProperty("digest")
    private String digest;

}
