package com.itheima.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午2:34:07
*/
@SpringBootApplication
@EnableEurekaClient
public class TimeApplication {
	
    public static void main(String[] args) {
    	
		SpringApplication.run(TimeApplication.class, args);
    
    }

}
