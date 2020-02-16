package com.flash.framework.core.support.event.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件日志
 *
 * @author zhurg
 * @date 2019/5/8 - 下午6:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventLog implements Serializable {

    private static final long serialVersionUID = 106859278793643537L;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件
     */
    private String event;

    /**
     * 事件类
     */
    private String eventClass;
}