package com.online_study.base.exception;


import lombok.Data;

/**
 * 自定义的异常类
 */
@Data
public class OnlineStudyException extends RuntimeException{

    private String errMessage;

    public OnlineStudyException(String message) {
        super(message);
        this.errMessage = message;
    }

    public static void cast(String message) {
        throw new OnlineStudyException(message);
    }

    public static void cast(ErrorEnum error) {
        throw new OnlineStudyException(error.getErrMessage());
    }
}
