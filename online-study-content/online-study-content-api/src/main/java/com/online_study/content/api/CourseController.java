package com.online_study.content.api;

import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.model.dto.QueryCourseParamsDto;
import com.online_study.model.po.CourseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(value = "课程信息管理接口", tags = "课程信息管理接口") //对swagger接口文档的信息进行注释
@RestController
public class CourseController {

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {
        CourseBase courseBase = new CourseBase();
        courseBase.setName("测试名称");
        courseBase.setCreateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        List<CourseBase> courseBases = new ArrayList<>();
        courseBases.add(courseBase);
        PageResult<CourseBase> pageResult = new PageResult<CourseBase>(courseBases,10,1,10);

        return pageResult;
    }
}
