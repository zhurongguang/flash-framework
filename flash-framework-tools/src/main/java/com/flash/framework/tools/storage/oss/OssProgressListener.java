package com.flash.framework.tools.storage.oss;


import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;

import java.math.BigDecimal;

/**
 * OSS上传进度条
 *
 * @author zhurg
 * @date 2019/6/12 - 下午4:38
 */
public class OssProgressListener implements ProgressListener {

    /**
     * already write bytes
     */
    private long bytesWritten = 0;
    /**
     * file total bytes
     */
    private long totalBytes = -1;
    /**
     * percent scale
     */
    private int percentScale = 2;
    /**
     * oss object name
     */
    private String objectName;

    private ObjectStorageProgress objectStorageProgress;

    public OssProgressListener(String objectName, ObjectStorageProgress objectStorageProgress) {
        this.objectName = objectName;
        this.objectStorageProgress = objectStorageProgress;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        if (null != objectStorageProgress) {
            // current progress bytes
            long bytes = progressEvent.getBytes();
            // progress event type
            ProgressEventType eventType = progressEvent.getEventType();

            switch (eventType) {
                // sent file total bytes
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    break;
                // request byte transfer
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        // Calculation percent
                        double percent = (this.bytesWritten * 100.00 / this.totalBytes);
                        BigDecimal decimal = BigDecimal.valueOf(percent).setScale(percentScale, BigDecimal.ROUND_DOWN);
                        objectStorageProgress.progress(objectName, decimal.doubleValue(), this.totalBytes, this.bytesWritten);
                    }
                    break;
                // complete
                case TRANSFER_COMPLETED_EVENT:
                    objectStorageProgress.success(objectName);
                    break;
                // failed
                case TRANSFER_FAILED_EVENT:
                    objectStorageProgress.failed(objectName);
                    break;
                default:
                    break;
            }
        }
    }
}