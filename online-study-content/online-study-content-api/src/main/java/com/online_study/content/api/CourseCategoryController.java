package com.online_study.content.api;

import com.online_study.content.service.CourseCategoryService;
import com.online_study.model.dto.CourseCategoryTreeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程分类接口
 */
@RestController
public class CourseCategoryController {

    @Autowired
    CourseCategoryService courseCategoryService;

    /**
     * 前端的接口就是默认查询所有分类结果，没有传参
     */
    @GetMapping("course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryCategoryTree() {

        return courseCategoryService.queryCategoryTree("1");
    }
}
