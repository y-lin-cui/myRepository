package com.itheima.leyou.service;

import java.util.Map;

/**
 * @author:ylin
 * @tips:
 * @version 1
 * @Date:上午3:47:08
 */
public interface IStockService {

	/**
	 * 获取商品列表，主要为了展示商品列表页
	 * 
	 * @return Map，包含list，包含一个商品的map
	 */
	public Map<String, Object> getStockList();
	
	/**
	 * @Describe: 获取商品详细信息
	 * @author LIN
	 * @date 2020-02-08 03:34:38
	 */
	public Map<String, Object> getStock(String sku_id);
	
	/**  
	 * @Describe: 新增秒杀政策，并写入缓存
	 * @author LIN
	 * @date 2020-02-08 09:41:17 
	 */
	public Map<String, Object> insertLimitPolicy(Map<String, Object> policyInfo);

}
