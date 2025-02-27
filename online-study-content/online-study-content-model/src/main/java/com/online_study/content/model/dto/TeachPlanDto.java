package com.online_study.content.model.dto;

import com.online_study.content.model.po.Teachplan;
import com.online_study.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 课程计划信息模型类
 */
@Data
@ToString
public class TeachPlanDto extends Teachplan {

    // 课程计划媒资信息
    private TeachplanMedia teachplanMedia;

    // 子节点（小章节）集合
    private List<TeachPlanDto> teachPlanTreeNodes;
}
