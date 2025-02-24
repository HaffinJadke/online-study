package com.online_study.content.service;

import com.online_study.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * 课程分类查询接口
 */
public interface CourseCategoryService {

    List<CourseCategoryTreeDto> queryCategoryTree(String id);
}
