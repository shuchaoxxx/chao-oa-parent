package com.chao.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * ClassName: ServiceAuthApplication
 * Package: com.chao.auth
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/12 - 19:23
 */

@SpringBootApplication
@ComponentScan("com.chao")
public class ServiceAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class, args);
    }

}
