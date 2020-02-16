package com.flash.framework.tools.storage.qiniu;

import lombok.Data;

/**
 * @author zhurg
 * @date 2019/6/13 - 上午10:26
 */
@Data
public class QiniuConfigure {

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 存储空间名称
     */
    private String bucketName;


    private QiniuZone zone;
}