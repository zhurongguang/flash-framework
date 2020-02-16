package com.flash.framework.core.event;

import com.flash.framework.core.BaseTestService;
import com.flash.framework.core.support.event.EventService;
import com.flash.framework.core.support.event.EventSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午3:13
 */
public class EventTest extends BaseTestService {

    @Autowired
    private EventService eventService;

    @Test
    public void testSpring() {
        DemoEvent event = new DemoEvent("demo", EventSource.SPRING);
        event.setDemo("SpringDemo");
        eventService.publishEvent(event);
    }

    @Test
    public void testEventBus() {
        DemoEvent event = new DemoEvent("demo", EventSource.EVENT_BUS);
        event.setDemo("EventBusDemo");
        eventService.publishEvent(event);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }
}