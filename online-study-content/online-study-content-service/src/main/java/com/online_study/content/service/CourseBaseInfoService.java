package com.online_study.content.service;

import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.content.model.dto.AddCourseDto;
import com.online_study.content.model.dto.CourseBaseInfoDto;
import com.online_study.content.model.dto.EditCourseDto;
import com.online_study.content.model.dto.QueryCourseParamsDto;
import com.online_study.content.model.po.CourseBase;

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

    /**
     * 新增课程接口
     * @param companyId 机构id（用户单点登录获取）
     * @param addCourseDto （课程信息）
     * @return （课程创建后返回的信息）
     */
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * 根据id查询课程接口，这个接口也被createCourseBase的业务实现调用
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    /**
     * 修改课程信息
     * @param companyId 机构id（用户单点登录获取）
     * @param editCourseDto （修改课程信息）
     * @return （课程修改后返回的信息）
     */
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto);

    /**
     * 删除课程信息
     * @param companyId
     * @param id
     */
    public void deleteCourseBase(Long companyId, Long id);

}
