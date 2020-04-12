package com.flash.framework.demo.extension;

import com.flash.framework.core.support.extension.annotation.Extension;
import org.springframework.stereotype.Service;

/**
 * @author zhurg
 * @date 2020/1/29 - 下午4:40
 */
@Service
public class StorageBizService {

    @Extension("storage")
    private StorageService storageService;

    /**
     * 测试基于bizCode动态选择实现
     *
     * @param bizCode
     */
    public void save(String bizCode) {
        storageService.save(bizCode);
    }

    /**
     * 测试基于spel获取bizCode
     *
     * @param param
     */
    public void save(OssBizParam param) {
        storageService.save(param);
    }
}