package cn.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:下午12:30:21
*/
/**
 * 通过@EnableFeignClients来开启spring cloud feign的支持功能
 *
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class FeiApp {

    public static void main(String[] args) {
        SpringApplication.run(FeiApp.class, args);
    }

}