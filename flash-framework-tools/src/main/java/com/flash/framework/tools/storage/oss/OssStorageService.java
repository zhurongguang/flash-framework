package com.flash.framework.tools.storage.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.flash.framework.tools.storage.ObjectStorageResponse;
import com.flash.framework.tools.storage.StorageService;
import com.flash.framework.tools.storage.StorageServiceConfigure;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author zhurg
 * @date 2019/6/6 - 下午3:40
 */
@Slf4j
public class OssStorageService implements StorageService, ApplicationContextAware {

    private final StorageServiceConfigure storageServiceConfigure;

    private ObjectStorageProgress objectStorageProgress;

    private final OSSClient ossClient;

    public OssStorageService(StorageServiceConfigure storageServiceConfigure) {
        this.storageServiceConfigure = storageServiceConfigure;
        if (StringUtils.isBlank(storageServiceConfigure.getOss().getBucketName())) {
            throw new IllegalArgumentException("[Flash Framework] Storage properties media.oss.bucketName is null");
        }
        this.ossClient = ossClient();
    }


    @Override
    public ObjectStorageResponse upload(String objectName, byte[] bytes) throws Exception {
        upload(ossClient, new PutObjectRequest(bucketName(), objectName, new ByteArrayInputStream(bytes)), objectName);
        return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();
    }

    @Override
    public ObjectStorageResponse upload(String objectName, InputStream inputStream) throws Exception {
        upload(ossClient, new PutObjectRequest(bucketName(), objectName, inputStream), objectName);
        return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();
    }

    @Override
    public ObjectStorageResponse upload(String objectName, File file) throws Exception {
        upload(ossClient, new PutObjectRequest(bucketName(), objectName, file), objectName);
        return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();
    }

    @Override
    public void download(String objectName, String localFile) throws Exception {
        try {
            ossClient.getObject(new GetObjectRequest(bucketName(), objectName)
                    .withProgressListener(new OssProgressListener(objectName, objectStorageProgress)), new File(localFile));
        } catch (Exception e) {
            log.error("[Flash Framework] Storage download failed,cause:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void download(String objectName, File file) throws Exception {
        try {
            ossClient.getObject(new GetObjectRequest(bucketName(), objectName)
                    .withProgressListener(new OssProgressListener(objectName, objectStorageProgress)), file);
        } catch (Exception e) {
            log.error("[Flash Framework] Storage download failed,cause:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void delete(String objectName) throws Exception {
        try {
            ossClient.deleteObject(bucketName(), objectName);
        } catch (Exception e) {
            log.error("[Flash Framework] Storage delete failed,cause:{}", Throwables.getStackTraceAsString(e));
        }
    }

    private void upload(OSSClient ossClient, PutObjectRequest putObjectRequest, String objectName) {
        try {
            putObjectRequest.withProgressListener(new OssProgressListener(objectName, objectStorageProgress));
            ossClient.putObject(putObjectRequest);
        } catch (Exception e) {
            log.error("[Flash Framework] Storage upload failed,cause:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 创建bucket
     *
     * @param bucketName
     */
    public void createBucket(String bucketName) {
        createBucket(ossClient(), bucketName);
    }

    /**
     * 设置bucket权限
     *
     * @param bucketName
     * @param cannedACL
     */
    public void setBucketAcl(String bucketName, CannedAccessControlList cannedACL) {
        ossClient.setBucketAcl(bucketName, cannedACL);
    }

    /**
     * 删除bucket
     *
     * @param bucketName
     */
    public void deleteBucket(String bucketName) {
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 创建bucket
     *
     * @param ossClient
     * @param bucketName
     */
    private void createBucket(OSSClient ossClient, String bucketName) {
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
    }

    /**
     * 初始化ossClient
     *
     * @return
     */
    private OSSClient ossClient() {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(storageServiceConfigure.getAccessKeyId(), storageServiceConfigure.getAccessKeySecret());
        return new OSSClient(storageServiceConfigure.getOss().getEndpoint().getEndpoint(), credentialsProvider, storageServiceConfigure.getOss().getConfig());
    }

    @PreDestroy
    public void close(OSSClient ossClient) {
        if (null != ossClient) {
            ossClient.shutdown();
        }
    }

    protected String bucketName() {
        return storageServiceConfigure.getOss().getBucketName();
    }

    /**
     * 获取默认的文件地址
     * 使用endpoint外网地址进行组合
     *
     * @param objectName 对象文件名称
     * @return 文件访问地址
     */
    protected String getDefaultObjectUrl(String objectName) {
        return String.format("https://%s.%s/%s", bucketName(), storageServiceConfigure.getOss().getEndpoint().getEndpoint()
                .replace("http://", ""), objectName);
    }

    /**
     * 获取文件地址
     *
     * @param objectName 文件对象名称
     * @return 文件访问地址
     */
    protected String getObjectUrl(String objectName) {
        if (StringUtils.isNotBlank(storageServiceConfigure.getOss().getDomain())) {
            return String.format(storageServiceConfigure.getOss().getDomain() + "/%s", objectName);
        }
        return getDefaultObjectUrl(objectName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            objectStorageProgress = applicationContext.getBean(ObjectStorageProgress.class);
        } catch (Exception e) {

        }
    }
}