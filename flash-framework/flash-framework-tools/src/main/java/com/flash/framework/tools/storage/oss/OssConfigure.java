package com.flash.framework.tools.storage.oss;

import com.aliyun.oss.ClientConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author zhurg
 * @date 2019/6/6 - 下午3:39
 */
@Data
public class OssConfigure {

    @NestedConfigurationProperty
    private ClientConfiguration config;

    /**
     * oss存储空间所绑定的自定义域名，如果不配置，上传文件成功后返回默认格式化的文件访问路径
     */
    private OssRegion endpoint;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 存储空间名称
     */
    private String bucketName;
}