package com.chao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * ClassName: ServiceAuthApplication
 * Package: com.chao.auth
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/12 - 19:23
 */


//@ComponentScan("com.chao")
@SpringBootApplication
public class ServiceAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class, args);
    }

}
