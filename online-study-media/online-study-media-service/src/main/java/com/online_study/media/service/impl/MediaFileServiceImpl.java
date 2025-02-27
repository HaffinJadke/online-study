package com.online_study.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.online_study.base.exception.OnlineStudyException;
import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.media.mapper.MediaFilesMapper;
import com.online_study.media.model.dto.QueryMediaParamsDto;
import com.online_study.media.model.dto.UploadFileParamDto;
import com.online_study.media.model.dto.UploadFileResultDto;
import com.online_study.media.model.po.MediaFiles;
import com.online_study.media.service.MediaFileService;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 媒资文件管理
 */
@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    /**
     * 将类自己注入自己，就可以实现非事务控制的方法调用事务控制的方法
     */
    @Autowired
    MediaFileService transactionProxy;

    /**
     * 媒资文件查询
     */
    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {
        // 构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();

        // 分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);

        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();

        // 获取数据总数
        long total = pageResult.getTotal();

        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());

        return mediaListResult;
    }


    /**
     * 根据文件扩展名获取文件的contentType
     */
    private String getMimeType(String extension) {
        if(extension == null) {
            extension = "";
        }

        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch("extension");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //通用contentType类型，字节流
        if(extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }

        return mimeType;
    }

    /**
     * 获取文件存储路径
     */
    private String getFileUploadFolder () {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/")+"/";
        return folder;
    }

    private String getFileUploadMd5(String localFilePath) {
        File file = new File(localFilePath);
        if(!file.exists()) {
            OnlineStudyException.cast("文件不存在");
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String md5 = DigestUtils.md5Hex(fileInputStream);
            return md5;
        } catch (Exception e) {
            String errMessage = "获取文件存储路径出错，文件路径:" + localFilePath + " 错误信息:" + e.getMessage();
            OnlineStudyException.cast(errMessage);
        }
        return null;
    }

    @Autowired
    MinioClient minioClient;

    /**
     * 上传文件到minio
     * @param localFilePath 文件本地路径
     * @param mimeType 文件类型
     * @param bucket 桶
     * @param objectName 对象，前缀是文件上传路径
     */
    private void addMediaFileToMinio(String localFilePath, String mimeType, String bucket, String objectName) {
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .filename(localFilePath)
                    .object(objectName)
                    .contentType(mimeType)
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
        } catch (Exception e) {
            String errMessage = "上传文件出错，bucket:" + bucket + "objectName:" + objectName + " 错误信息:" + e.getMessage();
            OnlineStudyException.cast(errMessage);
        }
    }


    @Value("${minio.bucket.files}")
    private String mediafiles; // 存储视频以外文件的桶
    @Value("${minio.bucket.videofiles}")
    private String videofiles; // // 存储视频文件的桶

    /**
     * 事务控制只加在对数据的操作上，如果放到uploadFile方法中，因为有网络连接上传到minio的行为，可能会长时间占用数据库资源
     * 但是非事务控制的方法调用事务控制的方法会导致事务控制无法生效
     * 要使得事务控制生效，必须把MediaFileService整个类注入到类自己的属性中，同时将本方法提取为接口
     */
    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public MediaFiles addMediaFilesToDb(Long companyId, String md5, UploadFileParamDto uploadFileParamDto, String bucket, String objectName) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(md5);
        //如果文件信息不存在才添加到数据库，否则直接返回查找到的文件信息
        if(mediaFiles == null) {
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(uploadFileParamDto, mediaFiles);

            mediaFiles.setId(md5);
            mediaFiles.setCompanyId(companyId);
            //TODO:机构名称
            mediaFiles.setBucket(bucket);
            mediaFiles.setFilePath(objectName);
            mediaFiles.setFileId(md5);
            mediaFiles.setUrl("/" + bucket + "/" + objectName);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            mediaFiles.setAuditStatus("002003"); //默认图片不需要审核

            //TODO:上传文件时，对course_base和course_teacher两张表的修改是在前端完成的，事务回滚也生效
            int insert = mediaFilesMapper.insert(mediaFiles);
            if(insert<=0) {
                OnlineStudyException.cast("文件信息保存到数据库失败");
                return null;
            }
        }
        return mediaFiles;
    }

    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamDto uploadFileParamDto, String localFilePath) {

        //获取扩展名
        String filename = uploadFileParamDto.getFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        //得到mimetype
        String mimeType = getMimeType(extension);
        //对象存储路径约定为 年/月/日/md码.扩展名   md码是本地文件的md5值，因为上传后会丢包
        String objectFolder = getFileUploadFolder();
        String md5 = getFileUploadMd5(localFilePath);
        String objectName = objectFolder + md5 + extension;
        //文件上传到minio，如果文件路径及名字重复，会覆盖minio内原有的数据
        addMediaFileToMinio(localFilePath, mimeType, mediafiles, objectName);

        //文件信息保存到数据库
        //这里用类自己注入到自己的属性，从而实现非事务控制方法调用事务控制方法
        MediaFiles mediaFiles = transactionProxy.addMediaFilesToDb(companyId, md5, uploadFileParamDto, mediafiles, objectName);


        //准备返回结果的对象
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
        return uploadFileResultDto;
    }
}