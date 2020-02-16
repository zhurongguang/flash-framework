package com.flash.framework.core.support.event.log;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Date;

/**
 * 事件日志切面
 *
 * @author zhurg
 * @date 2019/5/10 - 下午5:09
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class EventLogAop {

    private final EventLogHandler eventLogHandler;

    @Before(value = "execution(* com.flash.framework.core.support.event.EventPublisher.publish(..))")
    public void eventPublish(JoinPoint joinPoint) {
        BaseEvent event = getArg(joinPoint);
        EventLog log = buildEventLog(event);
        eventLogHandler.createEventLog(log);
    }

    @After(value = "execution(* com.flash.framework.core.support.event.EventHandler.handler(..))")
    public void eventHandler(JoinPoint joinPoint) {
        BaseEvent event = getArg(joinPoint);
        EventLog log = buildEventLog(event);
        eventLogHandler.removeEventLog(log);
    }

    private BaseEvent getArg(JoinPoint joinPoint) {
        return (BaseEvent) joinPoint.getArgs()[0];
    }

    private EventLog buildEventLog(BaseEvent event) {
        EventLog log = new EventLog();
        log.setEventId(event.getEventId());
        log.setEvent(JSON.toJSONString(event));
        log.setEventClass(event.getClass().toString());
        log.setEventType(event.getEventType());
        log.setPublishTime(new Date());
        return log;
    }
}