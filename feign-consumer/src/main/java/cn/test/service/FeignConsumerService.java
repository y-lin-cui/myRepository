package cn.test.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:下午12:32:38
*/
/**
 * 通过@FeignClient注解指定服务名来绑定服务，这里的服务名字不区分大小写
 * 然后再通过@RequestMapping来绑定服务下的rest接口
 *
 */
@FeignClient(name="hello-service")
public interface FeignConsumerService{

    @RequestMapping("/hello")
    public void hello();
    
    @RequestMapping("/testProducer")
    String testConsumer( @RequestParam("param") String param );
}