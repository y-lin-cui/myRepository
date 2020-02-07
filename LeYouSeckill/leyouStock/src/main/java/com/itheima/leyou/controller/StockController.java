package com.itheima.leyou.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.itheima.leyou.service.IStockService;

/**
* @author:ylin
* @tips:
* @version 1
* @Date:上午3:52:37
*/
@RestController
public class StockController {
	
	@Autowired
	private IStockService iStockService;
	
	 //获取所有商品数据
    @RequestMapping(value = "/getStockList")
    public Map<String, Object> getStockList(){
        return iStockService.getStockList();
    }
    
    //获取某商品详情
    @RequestMapping(value = "/getStock/{sku_id}")
    public Map<String, Object> getStock(@PathVariable("sku_id") String sku_id){
        return iStockService.getStock(sku_id);
    }

   

	
}
