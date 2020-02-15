package com.itheima.leyou.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
	
	@Bean
    public Queue queueOrder(){
        return new Queue("order_queue", true);
    }
	
	@Bean
    public Queue queueOrderStatus(){
        return new Queue("order_status_queue", true);
    }
}
