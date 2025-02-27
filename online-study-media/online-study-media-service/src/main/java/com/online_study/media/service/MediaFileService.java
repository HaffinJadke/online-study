package com.online_study.media.service;

import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.media.model.dto.QueryMediaParamsDto;
import com.online_study.media.model.po.MediaFiles;

public interface MediaFileService {

    /**
     * 媒资文件查询方法
    */
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

}