package com.online_study.base.exception;

import com.sun.org.apache.xalan.internal.xsltc.runtime.ErrorMessages_sk;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常处理类
 */
@Slf4j
@RestControllerAdvice //异常处理注解，返回json类型
public class GlobalExceptionHandler {

    /**
     * 对项目的自定义异常进行处理
     */
    @ExceptionHandler(OnlineStudyException.class) //指定要捕获的自定义异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //设置异常状态码（内部服务器错误）
    public RestErrorResponse customException(OnlineStudyException e) {
        //记录异常
        log.error("【项目异常】{}", e.getErrMessage(), e);
        //解析异常信息
        RestErrorResponse errorResponse = new RestErrorResponse(e.getErrMessage());
        return errorResponse;
    }

    /**
     * 对@validated注解抛出的异常进行处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) //指定要捕获的自定义异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //设置异常状态码（内部服务器错误）
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        List<String> errors = new ArrayList<>();
        //可能返回多个注解错误，这里用getFieldErrors的stream流进行遍历
        bindingResult.getFieldErrors().stream().forEach(item -> {
            errors.add(item.getDefaultMessage());
        });
        //拼接错误信息
        String errMessage = StringUtils.join(errors, ",");
        //记录异常
        log.error("【属性校验异常】{}", errMessage);
        //解析异常信息
        RestErrorResponse errorResponse = new RestErrorResponse(errMessage);
        return errorResponse;
    }

    /**
     * 非自定义异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        //记录异常
        log.error("【系统异常】{}", e.getMessage(), e);
        //解析异常信息
        RestErrorResponse errorResponse = new RestErrorResponse(ErrorEnum.UNKNOWN_ERROR.getErrMessage());
        return errorResponse;
    }
}
