package com.flash.framework.tools.storage;

import com.flash.framework.tools.storage.oss.OssStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author zhurg
 * @date 2019/6/6 - 下午3:37
 */
@Configuration
@ConditionalOnProperty(prefix = "storage", name = "enable", havingValue = "true")
@EnableConfigurationProperties(StorageServiceConfigure.class)
public class StorageConfiguration {

    @Bean(name = "storageService")
    @ConditionalOnMissingBean(StorageService.class)
    public StorageService ossStorageService(StorageServiceConfigure storageServiceConfigure) {
        Assert.notNull(storageServiceConfigure.getOss(), "[Flash Framework] oss properties storage.oss is null");
        return new OssStorageService(storageServiceConfigure);
    }

}