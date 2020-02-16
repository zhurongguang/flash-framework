package com.flash.framework.tools.push;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 推送消息
 *
 * @author zhurg
 * @date 2019/6/13 - 下午4:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushMessage implements Serializable {

    private static final long serialVersionUID = 6224243598986856023L;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 弹窗内容
     */
    private String alert;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private int messageType;

    /**
     * 自定义业务字段
     */
    private Map<String, String> extras;

    /**
     * 消息存活时间(默认一天)
     */
    private long timeToAlive = 86400L;

}