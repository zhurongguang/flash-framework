package com.flash.framework.demo.extension;

import com.flash.framework.core.support.extension.annotation.ExtensionProvider;

/**
 * @author zhurg
 * @date 2020/1/29 - 下午4:38
 */
@ExtensionProvider(group = "storage", bizCode = "oss")
public class OssStorageService implements StorageService {

    @Override
    public void save(String bizCode) {
        System.out.println("oss save");
    }

    @Override
    public void save(OssBizParam param) {
        System.out.println("oss save");
    }
}