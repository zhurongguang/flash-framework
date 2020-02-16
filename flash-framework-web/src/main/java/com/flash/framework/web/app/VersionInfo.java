package com.flash.framework.web.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhurg
 */
@Data
@ApiModel("版本实体")
public class VersionInfo implements Serializable {

    private static final long serialVersionUID = -3812918078605818267L;
    @ApiModelProperty("app版本号")
    private String appVersion;
    @ApiModelProperty("docVersion")
    private String docVersion;
    @ApiModelProperty("版本名称")
    private String appVersionName;
}
