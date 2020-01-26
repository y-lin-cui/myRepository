package cn.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午9:27:18
*/
/**
 * 
 * @EnableEurekaServer
 * 用来指定该项目为Eureka的服务注册中心
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApp {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApp.class, args);
    }
}