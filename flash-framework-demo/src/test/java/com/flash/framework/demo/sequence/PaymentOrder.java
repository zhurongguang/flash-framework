package com.flash.framework.demo.sequence;

import com.flash.framework.core.support.sequence.generator.IDRule;
import lombok.Data;

/**
 * @author zhurg
 * @date 2021/6/25 - 下午9:21
 */
@Data
@IDRule(prefix = "P+#paymentType+#shopId", step = 10000L, initValue = 10000000L)
public class PaymentOrder {

    private Long shopId;

    private Integer paymentType;
}