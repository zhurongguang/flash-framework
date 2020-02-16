package com.flash.framework.core.support.processor;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务处理上下文
 *
 * @author zhurg
 * @date 2018/11/7 - 下午1:01
 */
@Data
public abstract class BizProcessorContext implements Serializable {

    private static final long serialVersionUID = 5336185959988397476L;
}