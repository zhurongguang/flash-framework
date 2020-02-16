package com.flash.framework.tools.push.jpush;

import lombok.Data;

/**
 * @author zhurg
 * @date 2019/6/14 - 下午1:40
 */
@Data
public class JpushConfigure {

    /**
     * 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
     */
    private boolean apnsProduction;

    private boolean enable;
}