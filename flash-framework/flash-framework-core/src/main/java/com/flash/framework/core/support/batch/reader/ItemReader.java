package com.flash.framework.core.support.batch.reader;

import java.util.List;

/**
 * 数据读取接口
 *
 * @author zhurg
 * @date 2019/11/14 - 上午10:38
 */
public interface ItemReader<T> {

    List<T> read() throws Exception;
}