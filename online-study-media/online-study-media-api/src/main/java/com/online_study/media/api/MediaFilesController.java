package com.online_study.media.api;

import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.media.model.dto.QueryMediaParamsDto;
import com.online_study.media.model.po.MediaFiles;
import com.online_study.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
