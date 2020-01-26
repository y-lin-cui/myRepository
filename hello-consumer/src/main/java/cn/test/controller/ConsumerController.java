package cn.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午10:18:04
*/
@RestController
public class ConsumerController {

    //这里注入的restTemplate就是在com.sam.ConsumerApp中通过@Bean配置的实例
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/hello-consumer")
    public String helloConsumer() {
        //调用hello-service服务，注意这里用的是服务名，而不是具体的ip+port
        restTemplate.getForObject("http://hello-service/hello", String.class);
        return "hello consumer finish !!!";
    }
}