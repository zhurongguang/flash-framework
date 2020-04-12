package com.flash.framework.tools.push;

import com.flash.framework.tools.push.jpush.JpushConfigure;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author zhurg
 * @date 2019/6/14 - 上午11:01
 */
@Data
@ConfigurationProperties(prefix = "push")
public class PushServiceConfigure {

    private String appKey;

    private String appSecret;

    @NestedConfigurationProperty
    private JpushConfigure jpush;
}