package com.online_study.system;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * <p>
 *     系统管理启动类
 * </p>
 *
 * @Description:
 */
@EnableScheduling
@EnableSwagger2Doc
@SpringBootApplication
@CrossOrigin //允许跨域
@EnableDiscoveryClient //开启服务发现
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
}