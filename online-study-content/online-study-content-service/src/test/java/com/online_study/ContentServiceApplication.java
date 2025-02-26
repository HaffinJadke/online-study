package com.online_study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 课程管理服务启动类
 * test里的启动类也会自动引用main里的yaml配置文件
 */
@SpringBootApplication
public class ContentServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ContentServiceApplication.class, args);
    }
}
