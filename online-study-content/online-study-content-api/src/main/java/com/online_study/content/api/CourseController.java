package com.online_study.content.api;

import com.online_study.base.exception.ValidationGroups;
import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.content.service.CourseBaseInfoService;
import com.online_study.content.model.dto.AddCourseDto;
import com.online_study.content.model.dto.CourseBaseInfoDto;
import com.online_study.content.model.dto.EditCourseDto;
import com.online_study.content.model.dto.QueryCourseParamsDto;
import com.online_study.content.model.po.CourseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "课程信息管理接口", tags = "课程信息管理接口") //对swagger接口文档的信息进行注释
@RestController
public class CourseController {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("课程分页查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {

        PageResult<CourseBase> pageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto);

        return pageResult;
    }

    /**
     * @Validate 注解激活AddCourseDto的属性校验，同时指定分组
     */
    @ApiOperation("新增课程接口")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Default.class) AddCourseDto addCourseDto) {

        //TODO：用户单点登录后自动获取所属机构的id
        //先编一个机构id
        CourseBaseInfoDto courseBase = courseBaseInfoService.createCourseBase(1232141425L, addCourseDto);
        return courseBase;
    }

    /**
     * 根据课程id查询课程信息的接口，是在课程修改前需要调用的
     */
    @ApiOperation("根据id查询课程信息接口")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId) {
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }

    /**
     * 根据id查询完课程信息后，再进行修改
     */
    @ApiOperation("修改课程信息接口")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated(ValidationGroups.Default.class) EditCourseDto editCourseDto) {
        //TODO：用户单点登录后自动获取所属机构的id
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.updateCourseBase(1232141425L, editCourseDto);
        return courseBaseInfoDto;
    }

    @ApiOperation("删除课程信息接口")
    @DeleteMapping("/course/{id}")
    public void removeCourseBase(@PathVariable Long id) {
        //TODO:机构认证
        Long companyId = 1232141425L;
        courseBaseInfoService.deleteCourseBase(companyId, id);
    }
}
