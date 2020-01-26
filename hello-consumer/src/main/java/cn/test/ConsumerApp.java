package cn.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午10:16:11
*/
@SpringCloudApplication
public class ConsumerApp {


    //@Bean 应用在方法上，用来将方法返回值设为为bean
    @Bean
    @LoadBalanced  //@LoadBalanced实现负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}