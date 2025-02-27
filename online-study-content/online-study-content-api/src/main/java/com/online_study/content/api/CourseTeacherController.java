package com.online_study.content.api;

import com.online_study.content.service.CourseTeacherService;
import com.online_study.content.model.po.CourseTeacher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value="课程教师接口", tags="课程教师接口")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    /**
     * 根据课程id查询教师信息
     * @param id 课程id
     * @return
     */
    @ApiOperation("查询教师信息")
    @GetMapping("/courseTeacher/list/{id}")
    public List<CourseTeacher> getTeacherInfo(@PathVariable Long id) {
        List<CourseTeacher> teachers = courseTeacherService.getTeacherInfo(id);
        return teachers;
    }

    /**
     * 这里前端新增和修改调用的是同一个接口
     * @param courseTeacher
     * @return
     */
    @ApiOperation("新增/修改教师信息")
    @PostMapping("/courseTeacher")
    public CourseTeacher addOrModifyTeacher(@RequestBody CourseTeacher courseTeacher) {

        //TODO:登录机构id，只有自己机构的课程才可以添加教师信息
        Long companyId = 1232141425L;
        CourseTeacher courseTeacherNew = courseTeacherService.addOrModifyTeacher(companyId, courseTeacher);
        return courseTeacherNew;
    }

    /**
     * 删除教师信息
     * @param courseId 课程id
     * @param id 教师id
     */
    @ApiOperation("删除教师信息")
    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void removeCourseTeacher(@PathVariable Long courseId, @PathVariable Long id) {
        //TODO：登陆机构id认证
        Long companyId = 1232141425L;
        courseTeacherService.removeCourseTeacher(courseId, id, companyId);

    }

}
