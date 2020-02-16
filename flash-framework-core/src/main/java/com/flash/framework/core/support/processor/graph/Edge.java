package com.flash.framework.core.support.processor.graph;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhurg
 * @date 2019/8/23 - 上午11:36
 */
@Data
public class Edge implements Serializable {

    private static final long serialVersionUID = -6444941883322252899L;

    /**
     * 前驱
     */
    private String predecessor;

    /**
     * 后继
     */
    private String successor;
}