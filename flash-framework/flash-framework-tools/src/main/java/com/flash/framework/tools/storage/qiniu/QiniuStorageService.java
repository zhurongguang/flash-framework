package com.flash.framework.tools.storage.qiniu;

import cn.hutool.http.HttpUtil;
import com.flash.framework.tools.storage.ObjectStorageResponse;
import com.flash.framework.tools.storage.StorageService;
import com.flash.framework.tools.storage.StorageServiceConfigure;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.ZoneReqInfo;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author zhurg
 * @date 2019/6/13 - 上午10:29
 */
public class QiniuStorageService implements StorageService {

    private Configuration configuration;

    private Auth auth;

    private BucketManager bucketManager;

    private UploadManager uploadManager;

    private StorageServiceConfigure storageServiceConfigure;

    public QiniuStorageService(StorageServiceConfigure storageServiceConfigure) {
        this.storageServiceConfigure = storageServiceConfigure;
        this.auth = Auth.create(storageServiceConfigure.getAccessKeyId(), storageServiceConfigure.getAccessKeySecret());
        //自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        this.configuration = new Configuration(storageServiceConfigure.getQiniu().getZone().getZone());
        //创建上传对象
        this.bucketManager = new BucketManager(auth, this.configuration);
        this.uploadManager = new UploadManager(this.configuration);
        if (StringUtils.isBlank(storageServiceConfigure.getQiniu().getBucketName())) {
            throw new IllegalArgumentException("[Flash Framework] Storage properties media.qiniu.bucketName can not be null!");
        }
    }


    @Override
    public ObjectStorageResponse upload(String objectName, byte[] bytes) throws Exception {
        Response response = uploadManager.put(bytes, objectName, token());
        if (response.isOK()) {
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getUrl(putRet.key)).build();
        }
        throw new RuntimeException(response.toString());
    }

    @Override
    public ObjectStorageResponse upload(String objectName, InputStream inputStream) throws Exception {
        return upload(objectName, IOUtils.toByteArray(inputStream));
    }

    @Override
    public ObjectStorageResponse upload(String objectName, File file) throws Exception {
        Response response = uploadManager.put(file, objectName, token());
        if (response.isOK()) {
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return ObjectStorageResponse.builder().objectName(objectName).objectUrl(getUrl(putRet.key)).build();
        }
        throw new RuntimeException(response.toString());
    }

    @Override
    public void download(String objectName, String localFile) throws Exception {
        HttpUtil.downloadFile(getUrl(objectName), localFile);
    }

    @Override
    public void download(String objectName, File file) throws Exception {
        HttpUtil.downloadFile(getUrl(objectName), file);
    }

    @Override
    public void delete(String objectName) throws Exception {
        Response response = bucketManager.delete(bucketName(), objectName);
        if (!response.isOK()) {
            throw new RuntimeException(response.toString());
        }
    }

    public FileInfo getFileInfo(String objectName) throws QiniuException {
        return bucketManager.stat(bucketName(), objectName);
    }

    protected String getUrl(String key) {
        return String.format("%s/%s", storageServiceConfigure.getQiniu().getZone().getZone()
                .getUpBackupHttp(new ZoneReqInfo(storageServiceConfigure.getAccessKeyId(), storageServiceConfigure.getAccessKeySecret())), key);
    }

    protected String token() {
        return auth.uploadToken(bucketName());
    }

    protected String bucketName() {
        return storageServiceConfigure.getQiniu().getBucketName();
    }
}