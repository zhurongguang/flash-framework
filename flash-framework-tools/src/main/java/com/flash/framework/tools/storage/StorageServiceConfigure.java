package com.flash.framework.tools.storage;

import com.flash.framework.tools.storage.oss.OssConfigure;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author zhurg
 * @date 2019/6/6 - 下午3:34
 */
@Data
@ConfigurationProperties(prefix = "storage")
public class StorageServiceConfigure {

    private boolean enable;

    private String accessKeyId;

    private String accessKeySecret;

    @NestedConfigurationProperty
    private OssConfigure oss;
}