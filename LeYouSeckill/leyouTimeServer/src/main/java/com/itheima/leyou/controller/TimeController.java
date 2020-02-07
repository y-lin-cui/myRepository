package com.itheima.leyou.controller;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午2:36:15
*/
@RestController
public class TimeController {
	@RequestMapping(value = "/getTime")
    public String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
}
