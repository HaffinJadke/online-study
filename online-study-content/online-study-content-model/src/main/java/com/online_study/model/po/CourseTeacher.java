package com.online_study.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName course_teacher
 */
@TableName(value ="course_teacher")
@Data
public class CourseTeacher {
    private Long id;

    private Long courseId;

    private String teacherName;

    private String position;

    private String introduction;

    private String photograph;

    private Date createDate;
}