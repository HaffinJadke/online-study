package com.online_study.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.content.mapper.CourseBaseMapper;
import com.online_study.content.service.CourseBaseInfoService;
import com.online_study.model.dto.QueryCourseParamsDto;
import com.online_study.model.po.CourseBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {

        //mybatis-plus的wrapper条件（根据课程名、审核状态和发布状态查询）
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()),
                        CourseBase::getName, courseParamsDto.getCourseName())
                .eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()),
                        CourseBase::getAuditStatus,courseParamsDto.getAuditStatus())
                .eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()),
                        CourseBase::getStatus,courseParamsDto.getPublishStatus());

        //分页参数模型类转为mybatis-plus的分页参数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());

        //查询结果
        Page<CourseBase> result = courseBaseMapper.selectPage(page, wrapper);
        List<CourseBase> items = result.getRecords();
        long total = result.getTotal();

        //返回结果
        PageResult<CourseBase> pageResult = new PageResult<CourseBase>(items, total,
                pageParams.getPageNo(), pageParams.getPageSize());

        return pageResult;
    }
}
