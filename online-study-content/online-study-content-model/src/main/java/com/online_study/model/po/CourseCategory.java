package com.online_study.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName course_category
 */
@TableName(value ="course_category")
@Data
public class CourseCategory {
    private String id;

    private String name;

    private String label;

    private String parentid;

    private Integer isShow;

    private Integer orderby;

    private Integer isLeaf;
}