package com.online_study.content.api;

import com.online_study.model.dto.CourseCategoryTreeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程分类接口
 */
@RestController
public class CourseCategoryController {

    @GetMapping("course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryCategoryTree() {


        return null;
    }
}
