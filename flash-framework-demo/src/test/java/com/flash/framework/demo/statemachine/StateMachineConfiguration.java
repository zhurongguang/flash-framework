package com.flash.framework.demo.statemachine;

import com.flash.framework.core.support.fsm.StateMachine;
import com.flash.framework.core.support.fsm.StateMachineFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhurg
 * @date 2020/2/4 - 下午3:59
 */
@Configuration
public class StateMachineConfiguration {

    @Bean
    public StateMachineFactory demoStateMachineFactory() {
        return new StateMachineFactory() {
            @Override
            public void configure() {
                //构建订单业务状态流转
                StateMachine.StateMachineBuilder<String, String, Object> orderStateMachineBuilder = StateMachine.StateMachineBuilder.builder("order");
                orderStateMachineBuilder
                        .addTransition("not_paid", "paid", "paid")
                        .addTransition("paid", "delivery", "deliveryed")
                        .addTransition("deliveryed", "confirm", "confirmed")
                        .addTransition("not_paid", "cancel", "canceled", (event, to, context) -> {
                            System.out.println("event : " + event);
                            System.out.println("to : " + to);
                            System.out.println("context : " + context);
                        });
                //将订单状态添加到状态机
                addStateMachine(orderStateMachineBuilder.build());
            }
        };
    }
}