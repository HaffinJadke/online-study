package com.online_study.media.service;

import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.media.model.dto.QueryMediaParamsDto;
import com.online_study.media.model.dto.UploadFileParamDto;
import com.online_study.media.model.dto.UploadFileResultDto;
import com.online_study.media.model.po.MediaFiles;

public interface MediaFileService {

    /**
     * 媒资文件查询方法
    */
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /**
     * 上传图片到minio，并将文件信息保存到数据库
     * @param companyId 机构id
     * @param uploadFileParamDto 上传文件信息
     * @param localFilePath 本地文件路径
     */
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamDto uploadFileParamDto, String localFilePath);

    public MediaFiles addMediaFilesToDb(Long companyId, String md5, UploadFileParamDto uploadFileParamDto, String bucket, String objectName);

}