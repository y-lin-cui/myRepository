package com.itheima.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午2:26:54
*/
@SpringBootApplication
@EnableEurekaServer
public class ServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}