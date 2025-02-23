package com.online_study;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 课程管理服务启动类
 */
@SpringBootApplication
@EnableSwagger2Doc // 启用Swagger接口文档
public class ContentApplication {

    public static void main(String[] args) {

        SpringApplication.run(ContentApplication.class, args);
    }
}
