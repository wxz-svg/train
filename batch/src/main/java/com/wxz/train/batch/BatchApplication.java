package com.wxz.train.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@MapperScan("com.wxz.train.batch.mapper")
@ComponentScan("com.wxz.train")
@EnableFeignClients("com.wxz.train.batch.feign")
public class BatchApplication {

    // 配置日志
    private static final Logger log = LoggerFactory.getLogger(BatchApplication.class);

    /**
     * 应用的入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args){
        // 初始化Spring Boot应用
        SpringApplication application = new SpringApplication(BatchApplication.class);
        // 运行应用，并获取环境变量
        Environment environment = application.run(args).getEnvironment();
        // 记录应用启动成功的日志
        log.info("启动成功!");
        // 记录Member服务的访问地址
        log.info("Member地址: \thttp://127.0.0.1:{}{}", environment.getProperty("server.port"),environment.getProperty("server.servlet.context-path"));
    }
}