package com.wxz.train.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * 网关应用程序的入口点类。
 *
 * @@SpringBootApplication 标注此类为一个Spring Boot应用的启动类，自动包含@Controller、@Service、@Repository和@EnableAutoConfiguration注解。
 */
@SpringBootApplication
public class GateWayApplication {

    // 使用SLF4J提供的Logger来记录日志
    private static final Logger log = LoggerFactory.getLogger(GateWayApplication.class);

    /**
     * 应用程序的主入口函数。
     *
     * @param args 命令行传入的参数数组。
     */
    public static void main(String[] args){
        // 初始化SpringApplication，并指定此应用的配置类
        SpringApplication application = new SpringApplication(GateWayApplication.class);

        // 运行SpringApplication，并获取环境变量对象
        Environment environment = application.run(args).getEnvironment();

        // 记录应用启动成功的日志，并输出网关的访问地址
        log.info("启动成功!");
        log.info("网关地址: \thttp://127.0.0.1:{}", environment.getProperty("server.port"));
    }
}