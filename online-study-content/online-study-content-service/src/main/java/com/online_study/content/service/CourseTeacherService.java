package com.online_study.content.service;

import com.online_study.model.po.CourseTeacher;

import java.util.List;

public interface CourseTeacherService {

    List<CourseTeacher> getTeacherInfo(Long id);

    CourseTeacher addOrModifyTeacher(Long companyId, CourseTeacher courseTeacher);

    void removeCourseTeacher(Long courseId, Long id, Long companyId);

}
