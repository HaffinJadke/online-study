package com.online_study.content.model.dto;

import com.online_study.base.exception.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 添加课程时接收参数的模型类，包括CourseBase和CourseMarket两张表的信息，部分字段采用默认值
 */
@Data
@ApiModel(value="AddCourseDto", description="新增课程基本信息")
public class AddCourseDto {

    /**
     * groups属性可以在花括号内加多个分组
     * @NotEmpty(message = "课程名称不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
     * 但是groups的默认值为空，即分组情况下不会执行校验
     * 所以在ValidationGroups分组类下加上一个默认分组
     */
    @NotEmpty(message = "课程名称不能为空", groups = {ValidationGroups.Default.class})
    @ApiModelProperty(value = "课程名称", required = true)
    private String name;

    @NotEmpty(message = "适用人群不能为空", groups = {ValidationGroups.Default.class})
    @ApiModelProperty(value = "适用人群", required = true)
    private String users;

    @ApiModelProperty(value = "课程标签")
    private String tags;

    @NotEmpty(message = "课程大分类不能为空", groups = {ValidationGroups.Default.class})
    @ApiModelProperty(value = "大分类", required = true)
    private String mt;

    @NotEmpty(message = "课程小分类不能为空", groups = {ValidationGroups.Default.class})
    @ApiModelProperty(value = "小分类", required = true)
    private String st;

    @NotEmpty(message = "课程等级不能为空", groups = {ValidationGroups.Default.class})
    @ApiModelProperty(value = "课程等级", required = true)
    private String grade;

    @ApiModelProperty(value = "教学模式（录播，直播）", required = true)
    private String teachmode;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "课程图片", required = true)
    private String pic;

    @NotEmpty(message = "收费规则不能为空", groups = {ValidationGroups.Default.class})
    @ApiModelProperty(value = "收费规则，对应数据字典", required = true)
    private String charge;

    @ApiModelProperty(value = "价格")
    private Float price;

    @ApiModelProperty(value = "原价")
    private Float originalPrice;

    @ApiModelProperty(value = "qq")
    private String qq;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "有效期天数")
    private Integer validDays;
}
