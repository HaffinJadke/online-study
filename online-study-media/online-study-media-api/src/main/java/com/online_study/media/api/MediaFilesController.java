package com.online_study.media.api;

import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.media.model.dto.QueryMediaParamsDto;
import com.online_study.media.model.dto.UploadFileParamDto;
import com.online_study.media.model.dto.UploadFileResultDto;
import com.online_study.media.model.po.MediaFiles;
import com.online_study.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 媒资文件管理接口
 */
@Api(value = "媒资文件管理接口",tags = "媒资文件管理接口")
@RestController
public class MediaFilesController {

    @Autowired
    MediaFileService mediaFileService;

    @ApiOperation("媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        //TODO:机构认证
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId,pageParams,queryMediaParamsDto);
    }

    /**
     * consumes指定前端上传文件的类型为multipart/form-data
     * RequestPart指定了请求中文件部分的名称
     */
    @ApiOperation("上传图片")
    @RequestMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile filedata) throws IOException {
        //TODO:机构认证
        Long companyId = 1232141425L;

        //文件上传参数时的请求参数
        UploadFileParamDto uploadFileParamDto = new UploadFileParamDto();
        uploadFileParamDto.setFilename(filedata.getOriginalFilename()); //原始文件名称
        uploadFileParamDto.setFileSize(filedata.getSize()); //原始文件大小
        uploadFileParamDto.setFileType("001001"); //数据字典中定义为图片

        //文件存储在系统的临时文件中，要从filedata中提取出来
        File tempFile = File.createTempFile("minio", ".temp");
        filedata.transferTo(tempFile);
        String localFilePath = tempFile.getAbsolutePath(); //获取文件在系统中的路径

        UploadFileResultDto uploadFileResultDto = mediaFileService.uploadFile(companyId, uploadFileParamDto, localFilePath);
        return uploadFileResultDto;
    }
}
