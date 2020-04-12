package com.flash.framework.demo.init;

import com.flash.framework.core.spring.init.InitListenerHandler;
import com.flash.framework.core.spring.init.SpringBootInitHandler;
import org.springframework.context.ApplicationContext;

/**
 * @author zhurg
 * @date 2020/3/1 - 9:27 PM
 */
@SpringBootInitHandler(order = 2, async = true)
public class AsyncDemoInitListenerHandler implements InitListenerHandler {

    @Override
    public void handle(ApplicationContext applicationContext) {
        System.out.println("AsyncDemoInitListenerHandler do handle");
    }
}
