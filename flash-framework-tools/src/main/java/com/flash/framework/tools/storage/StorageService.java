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
     * @throws Exception
     */
    ObjectStorageResponse upload(String objectName, byte[] bytes) throws Exception;

    /**
     * 上传输入流中的数据
     *
     * @param objectName  存储对象名称
     * @param inputStream 数据流对象
     * @return
     * @throws Exception
     */
    ObjectStorageResponse upload(String objectName, InputStream inputStream) throws Exception;

    /**
     * 上传文件
     *
     * @param objectName 存储对象名称
     * @param file       文件
     * @return
     * @throws Exception
     */
    ObjectStorageResponse upload(String objectName, File file) throws Exception;

    /**
     * 下载文件
     *
     * @param objectName 存储对象名称
     * @param localFile  本地文件路径
     * @throws Exception
     */
    void download(String objectName, String localFile) throws Exception;

    /**
     * 下载文件
     *
     * @param objectName 存储对象名称
     * @param file       本地文件
     * @throws Exception
     */
    void download(String objectName, File file) throws Exception;

    /**
     * 删除文件
     *
     * @param objectName 存储对象名称
     * @throws Exception
     */
    void delete(String objectName) throws Exception;
}