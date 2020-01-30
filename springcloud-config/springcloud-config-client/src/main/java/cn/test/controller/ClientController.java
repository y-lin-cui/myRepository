package cn.test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:下午9:50:16
*/
@RefreshScope
@RestController
public class ClientController { 
    /**
     * 通过@Value 来讲配置文件中的值写入到代码中
     */
	@Value("${from}")
    private String from;

    @RequestMapping("/getGitMessage")
    public String from() {
        return from;
    }

}
