package com.flash.framework.core.support.event.log;

import java.util.List;
import java.util.Map;

/**
 * 事件日志处理
 *
 * @author zhurg
 * @date 2019/5/8 - 下午6:21
 */
public interface EventLogHandler {

    /**
     * 记录事件
     *
     * @param log
     */
    void createEventLog(EventLog log);

    /**
     * 删除事件
     *
     * @param log
     */
    void removeEventLog(EventLog log);

    /**
     * 查询事件
     *
     * @param params 查询参数
     * @return
     */
    List<EventLog> queryEventLogs(Map<String, Object> params);
}