package com.flash.framework.core.extension;

import com.flash.framework.core.BaseTestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhurg
 * @date 2020/1/29 - 下午4:42
 */
public class ExtensionTest extends BaseTestService {

    @Autowired
    private StorageBizService storageBizService;

    @Test
    public void test1() {
        storageBizService.save("oss");
        storageBizService.save("other");
    }

    @Test
    public void test2() {
        OssBizParam param = new OssBizParam();
        param.setStorage("oss");
        storageBizService.save(param);
        param.setStorage("other");
        storageBizService.save(param);
    }
}