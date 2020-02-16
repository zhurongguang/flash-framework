package com.flash.framework.tools.push;

import com.flash.framework.tools.push.jpush.JpushService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhurg
 * @date 2019/6/14 - 下午3:52
 */
@Configuration
@ConditionalOnProperty(prefix = "push", name = "enable", havingValue = "true")
@EnableConfigurationProperties(PushServiceConfigure.class)
public class PushServiceConfiguration {

    @Bean(name = "pushService")
    @ConditionalOnProperty("push.jpush")
    public JpushService jpushService(PushServiceConfigure pushServiceConfigure) {
        return new JpushService(pushServiceConfigure);
    }
}