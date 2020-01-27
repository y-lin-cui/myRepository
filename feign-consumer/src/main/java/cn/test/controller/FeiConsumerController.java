package cn.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.test.service.FeignConsumerService;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:下午12:37:36
*/
@RestController
public class FeiConsumerController {

    @Autowired
    FeignConsumerService consumerService;

    @RequestMapping("feign-consumer")
    public String feignConsumer() {
        consumerService.hello();
        return "feign consumer call finished!!!";
    }
    
    @RequestMapping("/testFeignConsumer")
    public String getDate( @RequestParam String param ) {
//    	System.out.println("访问成功");
        return consumerService.testConsumer( param );
    }

}
