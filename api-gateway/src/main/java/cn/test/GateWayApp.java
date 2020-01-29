	package cn.test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import cn.test.back.filter.AccessFilter;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午1:15:50
*/
/**
 * @EnableZuulProxy 开启Zuul 的API网关服务功能
 *
 */
@EnableZuulProxy
@SpringCloudApplication
@EnableDiscoveryClient
public class GateWayApp {
	
	 //追加bean的实现 开启过滤
    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }
	
    public static void main(String[] args) {
        SpringApplication.run(GateWayApp.class, args);
    }
}