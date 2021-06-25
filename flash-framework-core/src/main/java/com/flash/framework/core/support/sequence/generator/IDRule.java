package com.flash.framework.core.support.sequence.generator;

import java.lang.annotation.*;

/**
 * 主键规则
 *
 * @author zhurg
 * @date 2021/6/25 - 下午1:55
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IDRule {

    /**
     * 前缀,支持表达式从数据中取值,prefix=123+#shop+#userId
     *
     * @return
     */
    String prefix() default "";

    /**
     * 序列初始值
     *
     * @return
     */
    long initValue() default 0L;

    /**
     * 序列步长
     *
     * @return
     */
    long step() default 1000L;
}