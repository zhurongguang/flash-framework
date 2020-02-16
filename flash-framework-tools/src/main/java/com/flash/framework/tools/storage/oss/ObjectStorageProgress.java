package com.flash.framework.tools.storage.oss;

/**
 * 上传进度处理回调接口
 *
 * @author zhurg
 * @date 2019/6/12 - 下午4:41
 */
public interface ObjectStorageProgress {

    /**
     * progress
     *
     * @param objectName          object name
     * @param percent             upload or download progress percent
     * @param totalBytes          total bytes
     * @param currentWrittenBytes already written bytes
     * @throws Exception
     */
    void progress(String objectName, double percent, long totalBytes, long currentWrittenBytes);

    /**
     * upload or download success
     *
     * @param objectName object name
     * @throws Exception ApiBoot Oss Exception
     */
    void success(String objectName);

    /**
     * upload or download failed
     *
     * @param objectName object name
     * @throws Exception ApiBoot Oss Exception
     */
    void failed(String objectName);
}