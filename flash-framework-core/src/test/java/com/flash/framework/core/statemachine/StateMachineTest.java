package com.flash.framework.core.statemachine;

import com.flash.framework.core.BaseTestService;
import com.flash.framework.core.support.fsm.StateMachineFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhurg
 * @date 2020/2/4 - 下午4:11
 */
public class StateMachineTest extends BaseTestService {

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Test
    public void test1() {
        System.out.println(stateMachineFactory.get("order").fire("not_paid", "paid", "付钱"));
        System.out.println(stateMachineFactory.get("order").fire("not_paid", "cancel", "取消"));
    }
}