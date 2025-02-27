package com.online_study.media;


import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *  minio功能测试
 */
public class MinioTest {

    //创建minio对象
    private MinioClient minioClient = MinioClient.builder()
                                        .endpoint("http://192.168.101.65:9000")
                                        .credentials("minioadmin", "minioadmin")
                                        .build();

    /**
     * 上传文件
     */
    @Test
    public void testUpload() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        //com.j256.simplemagic:simplemagic依赖提供了根据文件扩展名获取文件的contentType的方法
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".png");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //通用contentType类型，字节流
        if(extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }

        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket") //桶
                .filename("D:\\online-study\\mediafiles\\测试图片1.png") //指定本地文件路径
                .object("pictures/测试图片1.png") //指定文件上传路径，默认桶的根目录
                .contentType(mimeType)
                .build();

        //上传文件
        minioClient.uploadObject(uploadObjectArgs);

        //TODO:校验上传文件完整性：上传后再下载下来与原始的本地文件进行比较
    }

    /**
     * 删除文件
     */
    @Test
    public void testDelete() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //同上传，但是不需要文件路径，指定文件名即可
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                .object("pictures/测试图片1.png")
                .build();

        minioClient.removeObject(removeObjectArgs);
    }

    /**
     * 下载文件
     */
    @Test
    public void testQuery() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket("testbucket")
                .object("pictures/测试图片1.png")
                .build();

        //获取桶文件流
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        //新建本地文件流
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\online-study\\mediafiles\\测试下载图片1.png"));
        //复制文件流
        IOUtils.copy(inputStream, outputStream);

        //校验下载文件完整性
        //从桶中下载文件时通过网络流，会因为丢包等原因大概率md5不同，这里的校验需要与上传时的本地文件进行比较
        String input_md5 = DigestUtils.md5Hex(new FileInputStream(new File("D:\\online-study\\mediafiles\\测试图片1.png")));
        String output_md5 = DigestUtils.md5Hex(new FileInputStream(new File("D:\\online-study\\mediafiles\\测试下载图片1.png")));
        if(input_md5.equals(output_md5)) {
            System.out.println("下载成功");
        }
    }
}
