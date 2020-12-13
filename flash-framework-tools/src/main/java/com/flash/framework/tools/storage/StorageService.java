package com.flash.framework.tools.storage;

import java.io.File;
import java.io.InputStream;

/**
 * 流媒体服务
 *
 * @author zhurg
 * @date 2019/6/6 - 下午3:33
 */
public interface StorageService {

    /**
     * 上传二进制数据流
     *
     * @param objectName 存储对象名称
     * @param bytes      二进制数据
     * @return
     */
    ObjectStorageResponse upload(String objectName, byte[] bytes);

    /**
     * 上传输入流中的数据
     *
     * @param objectName  存储对象名称
     * @param inputStream 数据流对象
     * @return
     */
    ObjectStorageResponse upload(String objectName, InputStream inputStream);

    /**
     * 上传文件
     *
     * @param objectName 存储对象名称
     * @param file       文件
     * @return
     */
    ObjectStorageResponse upload(String objectName, File file);

    /**
     * 下载文件
     *
     * @param objectName 存储对象名称
     * @param localFile  本地文件路径
     */
    void download(String objectName, String localFile);

    /**
     * 下载文件
     *
     * @param objectName 存储对象名称
     * @param file       本地文件
     */
    void download(String objectName, File file);

    /**
     * 删除文件
     *
     * @param objectName 存储对象名称
     */
    void delete(String objectName);
}