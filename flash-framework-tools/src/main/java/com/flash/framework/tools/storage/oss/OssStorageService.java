package com.flash.framework.tools.storage.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.STSAssumeRoleSessionCredentialsProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.auth.BasicCredentials;
import com.aliyuncs.profile.DefaultProfile;
import com.flash.framework.tools.storage.ObjectStorageResponse;
import com.flash.framework.tools.storage.StorageService;
import com.flash.framework.tools.storage.StorageServiceConfigure;
import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author zhurg
 * @date 2019/6/6 - 下午3:40
 */
@Slf4j
public class OssStorageService implements StorageService {

    private final StorageServiceConfigure storageServiceConfigure;

    private OssConfigure ossConfigure;

    @Getter
    private final OSSClient ossClient;

    public OssStorageService(StorageServiceConfigure storageServiceConfigure) {
        this.storageServiceConfigure = storageServiceConfigure;
        this.ossConfigure = storageServiceConfigure.getOss();
        if (StringUtils.isBlank(ossConfigure.getBucketName())) {
            throw new StorageException("[Flash Framework] Storage properties media.oss.bucketName is null");
        }
        this.ossClient = ossClient();
    }


    @Override
    public ObjectStorageResponse upload(String objectName, byte[] bytes) {
        upload(ossClient, new PutObjectRequest(ossConfigure.getBucketName(), objectName, new ByteArrayInputStream(bytes)));
        return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();
    }

    @Override
    public ObjectStorageResponse upload(String objectName, InputStream inputStream) {
        upload(ossClient, new PutObjectRequest(ossConfigure.getBucketName(), objectName, inputStream));
        return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();
    }

    @Override
    public ObjectStorageResponse upload(String objectName, File file) {
        upload(ossClient, new PutObjectRequest(ossConfigure.getBucketName(), objectName, file));
        return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getObjectUrl(objectName)).build();
    }

    @Override
    public void download(String objectName, String localFile) {
        try {
            ossClient.getObject(new GetObjectRequest(ossConfigure.getBucketName(), objectName), new File(localFile));
        } catch (Exception e) {
            log.error("[Flash Framework] Storage download failed,cause:{}", Throwables.getStackTraceAsString(e));
            throw new StorageException(e);
        }
    }

    @Override
    public void download(String objectName, File file) {
        try {
            ossClient.getObject(new GetObjectRequest(ossConfigure.getBucketName(), objectName), file);
        } catch (Exception e) {
            log.error("[Flash Framework] Storage download failed,cause:{}", Throwables.getStackTraceAsString(e));
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String objectName) {
        try {
            ossClient.deleteObject(ossConfigure.getBucketName(), objectName);
        } catch (Exception e) {
            log.error("[Flash Framework] Storage delete failed,cause:{}", Throwables.getStackTraceAsString(e));
            throw new StorageException(e);
        }
    }

    private void upload(OSSClient ossClient, PutObjectRequest putObjectRequest) {
        try {
            ossClient.putObject(putObjectRequest);
        } catch (Exception e) {
            log.error("[Flash Framework] Storage upload failed,cause:{}", Throwables.getStackTraceAsString(e));
            throw new StorageException(e);
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
        CredentialsProvider credentialsProvider;
        //不使用子账号角色访问
        if (StringUtils.isBlank(ossConfigure.getRoleArn())) {
            credentialsProvider = new DefaultCredentialProvider(storageServiceConfigure.getAccessKeyId(), storageServiceConfigure.getAccessKeySecret());
        } else {
            DefaultProfile profile = DefaultProfile.getProfile(ossConfigure.getEndpoint().getRegion());
            BasicCredentials basicCredentials = new BasicCredentials(storageServiceConfigure.getAccessKeyId(), storageServiceConfigure.getAccessKeySecret());
            credentialsProvider = new STSAssumeRoleSessionCredentialsProvider(basicCredentials, ossConfigure.getRoleArn(), profile)
                    .withRoleSessionName(ossConfigure.getRoleName());
        }
        return new OSSClient(ossConfigure.getEndpoint().getEndpoint(), credentialsProvider, ossConfigure.getConfig());
    }

    @PreDestroy
    public void close() {
        if (null != ossClient) {
            ossClient.shutdown();
        }
    }

    /**
     * 获取默认的文件地址
     * 使用endpoint外网地址进行组合
     *
     * @param objectName 对象文件名称
     * @return 文件访问地址
     */
    protected String getDefaultObjectUrl(String objectName) {
        return String.format("%s://%s.%s/%s", ossConfigure.getProtocol(), ossConfigure.getBucketName(), ossConfigure.getEndpoint().getEndpoint(), objectName);
    }

    /**
     * 获取文件地址
     *
     * @param objectName 文件对象名称
     * @return 文件访问地址
     */
    protected String getObjectUrl(String objectName) {
        //自定义域名访问
        if (StringUtils.isNotBlank(ossConfigure.getDomain())) {
            return String.format("%s/%s", ossConfigure.getDomain(), objectName);
        }
        return getDefaultObjectUrl(objectName);
    }
}