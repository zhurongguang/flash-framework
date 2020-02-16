package com.flash.framework.core.support.event.log;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;
import java.util.Map;

/**
 * Redis 时间日志记录实现
 *
 * @author zhurg
 * @date 2019/11/26 - 下午2:27
 */
public class RedisEventLogHandler implements EventLogHandler {

    private static final String EVENT_LOG_SCHEMA = "event_log";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void createEventLog(EventLog log) {
        redisTemplate.opsForHash().put(EVENT_LOG_SCHEMA, buildKey(log.getEventType(), log.getEventId()), log);
    }

    @Override
    public void removeEventLog(EventLog log) {
        redisTemplate.opsForHash().delete(EVENT_LOG_SCHEMA, buildKey(log.getEventType(), log.getEventId()));
    }

    @Override
    public List<EventLog> queryEventLogs(Map<String, Object> params) {
        Cursor<Map.Entry<String, EventLog>> cursor = redisTemplate.opsForHash().scan(EVENT_LOG_SCHEMA, ScanOptions.NONE);
        List<EventLog> datas = Lists.newArrayList();
        while (cursor.hasNext()) {
            datas.add(cursor.next().getValue());
        }
        return datas;
    }

    private String buildKey(String eventType, String eventId) {
        return String.format("%s:%s", eventType, eventId);
    }
}
