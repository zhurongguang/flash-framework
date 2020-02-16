package com.flash.framework.web.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备信息
 *
 * @author zhurg
 */
@Data
@ApiModel("设备信息实体")
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = -5333292716557916085L;

    @ApiModelProperty("平台")
    private String devicePlatform;
    @ApiModelProperty("设备")
    private String deviceDevice;
    @ApiModelProperty
    private String deviceModel;
    @ApiModelProperty("宽度")
    private String screenWidth;
    @ApiModelProperty("高度")
    private String screenHeight;
    @ApiModelProperty("系统版本")
    private String systemVersion;
    @ApiModelProperty("imei")
    private String imei;
    @ApiModelProperty("OpenUDID")
    private String OpenUDID;
}
