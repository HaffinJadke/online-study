package com.online_study.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName course_base
 */
@TableName(value ="course_base")
@Data
public class CourseBase {

    //TODO:前端有bug，MyBatis-plus使用insert方法时，这里的主键会用雪花算法，而前段读不到这么高的精度
    private Long id;

    private Long companyId;

    private String companyName;

    private String name;

    private String users;

    private String tags;

    private String mt;

    private String st;

    private String grade;

    private String teachmode;

    private String description;

    private String pic;

    private Date createDate;

    private Date changeDate;

    private String createPeople;

    private String changePeople;

    private String auditStatus;

    private String status;
}