package cn.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午9:35:45
*/
/***
 * 
 * @EnableDiscoveryClient
 * 让服务使用eureka服务器
 * 实现服务注册和发现
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class HelloApp {

    public static void main(String[] args) {

        SpringApplication.run(HelloApp.class, args);
    }

}