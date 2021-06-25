package com.flash.framework.demo.sequence;

import com.flash.framework.core.support.sequence.generator.IdGenerator;
import com.flash.framework.demo.BaseTestService;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhurg
 * @date 2021/6/25 - 下午9:20
 */
public class SequenceTest extends BaseTestService {

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void test() {
        Set<String> s1 = Sets.newConcurrentHashSet();
        Set<String> s2 = Sets.newConcurrentHashSet();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 1; i <= 10010; i++) {
            executorService.execute(() -> {
                String s = idGenerator.generate(new PayJournal());
                if (s1.contains(s)) {
                    System.out.println(s + "exists");
                } else {
                    s1.add(s);
                }
            });
        }

        for (int i = 1; i <= 10010; i++) {
            executorService.execute(() -> {
                PaymentOrder paymentOrder = new PaymentOrder();
                Random random = new Random();
                paymentOrder.setPaymentType(random.nextInt(1) + 1);
                paymentOrder.setShopId((long) random.nextInt(100));
                String s = idGenerator.generate(paymentOrder);
                if (s2.contains(s)) {
                    System.out.println(s + "exists");
                } else {
                    s2.add(s);
                }
            });
        }

        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(s1.size());
        System.out.println(s2.size());
    }
}