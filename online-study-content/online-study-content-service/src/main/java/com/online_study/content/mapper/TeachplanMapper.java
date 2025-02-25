package com.online_study.content.mapper;

import com.online_study.model.dto.TeachPlanDto;
import com.online_study.model.po.Teachplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author scq94
* @description 针对表【teachplan(课程计划)】的数据库操作Mapper
* @createDate 2025-02-22 22:01:38
* @Entity com.online_study.model.po.Teachplan
*/
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    //课程计划树形结构查询
    public List<TeachPlanDto> selectTreeNodes(Long courseId);
}




