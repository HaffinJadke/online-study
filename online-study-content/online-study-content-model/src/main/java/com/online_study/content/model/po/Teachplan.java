package com.online_study.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName teachplan
 */
@TableName(value ="teachplan")
@Data
public class Teachplan {
    private Long id;

    private String pname;

    private Long parentid;

    private Integer grade;

    private String mediaType;

    private Date startTime;

    private Date endTime;

    private String description;

    private String timelength;

    private Integer orderby;

    private Long courseId;

    private Long coursePubId;

    private Integer status;

    private String isPreview;

    private Date createDate;

    private Date changeDate;
}