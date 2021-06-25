package com.flash.framework.core.support.sequence;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhurg
 * @date 2021/6/25 - 上午10:38
 */
@Data
public class SequenceRange implements Serializable {

    private static final long serialVersionUID = 8805099891818534934L;

    private final long min;

    private final long max;

    private final AtomicLong value;

    private volatile boolean over = false;

    public SequenceRange(long min, long max) {
        this.min = min;
        this.max = max;
        this.value = new AtomicLong(min);
    }

    public long getAndIncrement() {
        long currentValue = this.value.getAndIncrement();
        if (currentValue > this.max) {
            this.over = true;
            return -1L;
        } else {
            return currentValue;
        }
    }
}