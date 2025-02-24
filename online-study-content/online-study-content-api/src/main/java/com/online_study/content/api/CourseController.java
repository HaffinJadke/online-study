package com.online_study.content.api;

import com.online_study.base.exception.ValidationGroups;
import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.content.service.CourseBaseInfoService;
import com.online_study.model.dto.AddCourseDto;
import com.online_study.model.dto.CourseCreateInfoDto;
import com.online_study.model.dto.QueryCourseParamsDto;
import com.online_study.model.po.CourseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "课程信息管理接口", tags = "课程信息管理接口") //对swagger接口文档的信息进行注释
@RestController
public class CourseController {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("课程查询接口")
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
    public CourseCreateInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Default.class) AddCourseDto addCourseDto) {

        //TODO：用户单点登录后自动获取所属机构的id
        //先编一个机构id
        return courseBaseInfoService.createCourseBase(1232141425L, addCourseDto);
    }
}
