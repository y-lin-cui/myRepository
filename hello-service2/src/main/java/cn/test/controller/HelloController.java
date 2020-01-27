package cn.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午9:36:36
*/
@RestController
public class HelloController {
	  Logger logger = LoggerFactory.getLogger(HelloController.class);

	    @Autowired
	    DiscoveryClient discoveryClient;
	    
	    @RequestMapping("/hello")
	    public String hello() {
	        ServiceInstance instance = discoveryClient.getLocalServiceInstance();
	        System.out.println("===============================service2被执行===============================");
	        //打印服务的服务id
	        logger.info("*********" + instance.getServiceId());
	        return "hello,this is hello-service";
	    }
	    
	    @RequestMapping( value = "/testProducer")
	    public String testProducer( @RequestParam String param ){
	        return "生产者接2收消息： " + param;
	    }
}
