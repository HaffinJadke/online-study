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

    // TODO:course_market表使用了自定义主键，否则调用insert方法时会默认用雪花算法覆盖本来正确的主键
    @TableId(type=IdType.INPUT) //主键自定义，否则insert时会用雪花算法或自增
    private Long id;

    private String charge;

    private Float price;

    private Float originalPrice;

    private String qq;

    private String wechat;

    private String phone;

    private Integer validDays;
}