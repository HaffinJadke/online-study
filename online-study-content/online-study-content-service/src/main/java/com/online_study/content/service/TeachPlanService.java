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

    /**
     * 删除课程计划
     * @param id
     */
    public void removeTeachplan(Long id);

    /**
     * 课程计划上移、下移，二者使用同一个接口，为真上移，为假下移
     * @param id
     * @param upOrDown
     */
    public void moveUpOrDownTeachplan(Long id, boolean upOrDown);

}
