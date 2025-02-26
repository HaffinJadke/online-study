package com.online_study.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.online_study.base.exception.OnlineStudyException;
import com.online_study.content.mapper.CourseBaseMapper;
import com.online_study.content.mapper.CourseTeacherMapper;
import com.online_study.content.service.CourseTeacherService;
import com.online_study.model.po.CourseBase;
import com.online_study.model.po.CourseTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;
    @Autowired
    private CourseBaseMapper courseBaseMapper;

    /**
     * 查询课程教师
     * @param id 课程id
     * @return
     */
    @Override
    public List<CourseTeacher> getTeacherInfo(Long id) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(CourseTeacher::getCourseId, id);
        List<CourseTeacher> teachers = courseTeacherMapper.selectList(queryWrapper);

        return teachers;
    }

    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public CourseTeacher addOrModifyTeacher(Long companyId, CourseTeacher courseTeacher) {
        CourseBase courseBase = courseBaseMapper.selectById(courseTeacher.getCourseId());
        if(courseBase == null) {
            OnlineStudyException.cast("课程不存在");
        }
        if(courseBase.getCompanyId().longValue() != companyId) {
            OnlineStudyException.cast("无权限修改其他机构课程的教师信息");
        }

        Long courseTeacherId = courseTeacher.getId();
        if(courseTeacherId == null) { //新增
            //前端未传入的字段信息手动加入
            courseTeacher.setCreateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            //TODO:前端接受雪花算法生成的主键会损失精度
            int row = courseTeacherMapper.insert(courseTeacher);
            if (row <= 0) {
                OnlineStudyException.cast("新增教师信息失败");
            }
        } else { //修改
            int row = courseTeacherMapper.updateById(courseTeacher);
            if(row <= 0) {
                OnlineStudyException.cast("修改教师信息失败");
            }
        }

        return courseTeacher;
    }

    /**
     * 删除教师信息
     * @param courseId 课程id
     * @param id 教师id
     * @param companyId 机构id
     */
    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public void removeCourseTeacher(Long courseId, Long id, Long companyId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null) {
            OnlineStudyException.cast("课程不存在");
        }
        if(courseBase.getCompanyId().longValue() != companyId) {
            OnlineStudyException.cast("无权限修改其他机构课程的教师信息");
        }

        int row = courseTeacherMapper.deleteById(id);
        if(row <= 0) {
            OnlineStudyException.cast("删除教师信息失败");
        }
    }
}
