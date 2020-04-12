package com.flash.framework.demo.event;

import com.alibaba.fastjson.JSON;
import com.flash.framework.core.support.event.EventHandler;
import com.google.common.eventbus.Subscribe;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author zhurg
 * @date 2019/11/26 - 下午3:15
 */
@Component
public class DemoEventHandler implements EventHandler<DemoEvent> {

    @Override
    @EventListener(classes = DemoEvent.class)
    @Subscribe
    public void handler(DemoEvent event) {
        System.out.println("accept event : " + JSON.toJSONString(event));
    }
}
