package com.online_study;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * 课程管理服务启动类
 */
@SpringBootApplication
@EnableSwagger2Doc // 启用Swagger接口文档
@CrossOrigin //允许跨域
@EnableDiscoveryClient //开启服务发现
public class ContentApplication {

    public static void main(String[] args) {

        SpringApplication.run(ContentApplication.class, args);
    }
}
