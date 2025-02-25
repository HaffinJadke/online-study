package com.online_study.model.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
public class EditCourseDto extends AddCourseDto {

    //TODO:前端有bug，MyBatis-plus使用insert方法时，这里的主键会用雪花算法，而前段读不到这么高的精度
    @ApiModelProperty(value = "课程id", required = true)
    private Long id;
}
