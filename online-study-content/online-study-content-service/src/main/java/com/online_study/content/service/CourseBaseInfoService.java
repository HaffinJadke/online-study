package com.online_study.content.service;

import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.model.dto.QueryCourseParamsDto;
import com.online_study.model.po.CourseBase;

/**
 * 课程信息管理接口
 */
public interface CourseBaseInfoService {

    /**
     * 课程分页查询接口
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
}
