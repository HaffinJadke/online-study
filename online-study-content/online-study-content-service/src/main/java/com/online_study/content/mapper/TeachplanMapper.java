package com.online_study.content.mapper;

import com.online_study.model.dto.TeachPlanDto;
import com.online_study.model.po.Teachplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author scq94
* @description 针对表【teachplan(课程计划)】的数据库操作Mapper
* @createDate 2025-02-22 22:01:38
* @Entity com.online_study.model.po.Teachplan
*/
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    //课程计划树形结构查询
    List<TeachPlanDto> selectTreeNodes(Long courseId);

    //查找同级课程计划的最大排序字段
    //mapper.xml中有多个参数，需要使用@Param注解指定参数名
    Integer selectMaxOrderby(@Param("parentId") Long parentId, @Param("courseId") Long courseId);

    //查找同级课程计划的最小排序字段
    Integer selectMinOrderby(@Param("parentId") Long parentId, @Param("courseId") Long courseId);

}