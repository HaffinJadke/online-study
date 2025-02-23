package com.online_study.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName course_market
 */
@TableName(value ="course_market")
@Data
public class CourseMarket {
    private Long id;

    private String charge;

    private Double price;

    private Double originalPrice;

    private String qq;

    private String wechat;

    private String phone;

    private Integer validDays;
}