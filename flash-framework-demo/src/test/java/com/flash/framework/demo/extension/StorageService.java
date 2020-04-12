package com.flash.framework.demo.extension;

import com.flash.framework.core.support.extension.annotation.BizCodeParam;

/**
 * @author zhurg
 * @date 2020/1/29 - 下午4:37
 */
public interface StorageService {

    void save(@BizCodeParam String bizCode);

    void save(@BizCodeParam(el = "#storage") OssBizParam param);
}