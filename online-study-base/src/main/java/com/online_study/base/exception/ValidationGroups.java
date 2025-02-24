package com.online_study.base.exception;

/**
 * 对@Validated注解的分组校验类
 */
public class ValidationGroups {
    public interface Default { };
    public interface Insert extends Default { };
    public interface Update extends Default { };
    public interface Delete extends Default { };
}
