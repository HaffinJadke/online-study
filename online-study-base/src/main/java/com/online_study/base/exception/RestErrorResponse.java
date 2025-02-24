package com.online_study.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 和前端约定的异常信息类，支持json格式序列化
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestErrorResponse implements Serializable {

    private String errMessage;

}
