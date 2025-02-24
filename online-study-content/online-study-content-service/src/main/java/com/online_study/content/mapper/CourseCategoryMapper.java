package com.online_study.content.mapper;

import com.online_study.model.dto.CourseCategoryTreeDto;
import com.online_study.model.po.CourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author scq94
* @description 针对表【course_category(课程分类)】的数据库操作Mapper
* @createDate 2025-02-22 22:00:55
* @Entity com.online_study.model.po.CourseCategory
*/
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    //sql递归查询课程分类，这里的返回类型不用list也可以，因为查找的是当前节点的数，只返回一个CourseCategoryTreeDto
    List<CourseCategoryTreeDto> selectCategoryTree(String id);
}