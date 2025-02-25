package com.online_study.content.service;

import com.online_study.model.dto.SaveTeachplanDto;
import com.online_study.model.dto.TeachPlanDto;

import java.util.List;

/**
 * 课程疾患管理接口
 */
public interface TeachPlanService {

    /**
     * 查询课程计划树形结构
     * @param courseId
     * @return
     */
    public List<TeachPlanDto> findTeachPlanTree(Long courseId);

    /**
     * 新增大章节、小章节，更改章节信息接口
     * @param saveTeachplanDto
     */
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto);
}
