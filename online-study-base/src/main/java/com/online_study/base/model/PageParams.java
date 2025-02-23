package com.online_study.base.model;

import lombok.*;

/**
 * 分页查询参数
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {

    private Long pageNo = 1L;
    private Long pageSize = 10L;
}
