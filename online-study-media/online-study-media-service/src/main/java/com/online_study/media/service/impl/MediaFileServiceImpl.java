package com.online_study.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.media.mapper.MediaFilesMapper;
import com.online_study.media.model.dto.QueryMediaParamsDto;
import com.online_study.media.model.po.MediaFiles;
import com.online_study.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 媒资文件管理
 */
@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

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
}