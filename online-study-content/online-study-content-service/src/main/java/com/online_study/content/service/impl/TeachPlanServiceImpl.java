package com.online_study.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.online_study.base.exception.OnlineStudyException;
import com.online_study.content.mapper.TeachplanMapper;
import com.online_study.content.service.TeachPlanService;
import com.online_study.model.dto.SaveTeachplanDto;
import com.online_study.model.dto.TeachPlanDto;
import com.online_study.model.po.Teachplan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class TeachPlanServiceImpl implements TeachPlanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Override
    public List<TeachPlanDto> findTeachPlanTree(Long courseId) {
        List<TeachPlanDto> teachPlanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachPlanDtos;
    }

    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过前端是否传入教学计划id判断是修改还是新增
        Long teachplanId = saveTeachplanDto.getId();
        //新增（前端传入数据的时候就已经定义了当前是大章节还是小章节）
        if(teachplanId == null) {
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);

            //确定排序字段（排序字段在数据库中从1开始）
            //select count(1) from teachplan where parent_id = parentid and course_id = courseid
            //新增的可成排序字段即为count+1
            //TODO:这里的排序字段应该得到同级节点的最大排序字段，再+1
            Long parentId = saveTeachplanDto.getParentid();
            Long courseId = saveTeachplanDto.getCourseId();
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper = queryWrapper.eq(Teachplan::getCourseId, courseId).eq(Teachplan::getParentid, parentId);
            Integer count = teachplanMapper.selectCount(queryWrapper);
            teachplan.setOrderby(count+1);

            teachplanMapper.insert(teachplan);
        } else {
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        }
    }
}
