package com.flash.framework.tools.storage;

import com.flash.framework.tools.storage.oss.OssStorageService;
import com.flash.framework.tools.storage.qiniu.QiniuStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhurg
 * @date 2019/6/6 - 下午3:37
 */
@Configuration
@ConditionalOnProperty(prefix = "media", name = "enable", havingValue = "true")
@EnableConfigurationProperties(StorageServiceConfigure.class)
public class StorageConfiguration {

    @Bean(name = "storageService")
    @ConditionalOnProperty("media.oss")
    public OssStorageService ossStorageService(StorageServiceConfigure storageServiceConfigure) {
        return new OssStorageService(storageServiceConfigure);
    }

    @Bean(name = "storageService")
    @ConditionalOnProperty("media.qiniu")
    public QiniuStorageService qiniuStorageService(StorageServiceConfigure storageServiceConfigure) {
        return new QiniuStorageService(storageServiceConfigure);
    }

}