package com.online_study.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.content.mapper.CourseBaseMapper;
import com.online_study.content.mapper.CourseCategoryMapper;
import com.online_study.content.mapper.TeachplanMapper;
import com.online_study.content.service.CourseBaseInfoService;
import com.online_study.content.service.CourseCategoryService;
import com.online_study.content.model.dto.CourseCategoryTreeDto;
import com.online_study.content.model.dto.QueryCourseParamsDto;
import com.online_study.content.model.dto.TeachPlanDto;
import com.online_study.content.model.po.CourseBase;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseContentTest {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    /**
     * 课程查询的mapper测试
     */
    @Test
    public void testCourseBaseMapper(){
        //条件查询，dto存了条件模型
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");

        //mybatis-plus的wrapper条件（根据课程名、审核状态和发布状态查询）
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()),
                    CourseBase::getName, courseParamsDto.getCourseName())
                .eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()),
                    CourseBase::getAuditStatus,courseParamsDto.getAuditStatus())
                .eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()),
                        CourseBase::getStatus,courseParamsDto.getPublishStatus());

        //分页参数模型类
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(3L);
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());

        //查询结果
        Page<CourseBase> result = courseBaseMapper.selectPage(page, wrapper);
        List<CourseBase> items = result.getRecords();
        long total = result.getTotal();

        //返回结果
        PageResult<CourseBase> pageResult = new PageResult<CourseBase>(items, total,
                pageParams.getPageNo(), pageParams.getPageSize());
        System.out.println(pageResult);
    }

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    /**
     * 课程查询的service测试
     */
    @Test
    public void testCourseInfoService() {
        //条件查询，dto存了条件模型
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");
        courseParamsDto.setAuditStatus("202004");
        courseParamsDto.setPublishStatus("203002");
        //分页参数模型类
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(3L);
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());

        PageResult<CourseBase> pageResult = courseBaseInfoService.queryCourseBaseList(pageParams, courseParamsDto);
        System.out.println(pageResult);
    }

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    /**
     * 课程分类的mapper测试
     */
    @Test
    public void testCourseCategoryMapper(){
        List<CourseCategoryTreeDto> courseCategoryTree = courseCategoryMapper.selectCategoryTree("1");
        System.out.println(courseCategoryTree);
    }

    @Autowired
    CourseCategoryService courseCategoryService;

    /**
     * 课程分类的service测试
     */
    @Test
    public void testCourseCategoryService(){
        List<CourseCategoryTreeDto> courseCategoryTree = courseCategoryService.queryCategoryTree("1");
        System.out.println(courseCategoryTree);
    }

    @Autowired
    TeachplanMapper teachplanMapper;

    /**
     * 测试课程计划查询接口
     */
    @Test void testSelectTreeNodes(){
        List<TeachPlanDto> teachPlanDtos = teachplanMapper.selectTreeNodes(117L);
        System.out.println(teachPlanDtos);
    }
}
