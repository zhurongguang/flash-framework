package com.flash.framework.core.event;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.event.log.EventLog;
import com.flash.framework.core.support.event.log.EventLogHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2019/11/26 - 下午3:20
 */
@Primary
@Component
public class TestEventLogHandler implements EventLogHandler {

    @Override
    public void createEventLog(EventLog log) {
        System.out.println("create eventLog : " + JSON.toJSONString(log));
    }

    @Override
    public void removeEventLog(EventLog log) {
        System.out.println("remove eventLog : " + JSON.toJSONString(log));
    }

    @Override
    public List<EventLog> queryEventLogs(Map<String, Object> params) {
        return null;
    }
}
