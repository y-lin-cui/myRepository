package com.itheima.leyou.dao;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author:ylin
 * @tips:
 * @version 1
 * @Date:上午3:43:40
 */
public interface IStockDao {
	
	/**
	 * 获取商品列表，主要为了展示商品列表页
	 * 
	 * @return list，包含一个商品的map
	 */
	public ArrayList<Map<String, Object>> getStockList();
	
	/**
	 * @Describe: 获取商品详细信息
	 * @author LIN
	 * @date 2020-02-08 03:34:38
	 */
	public ArrayList<Map<String, Object>> getStock(String sku_id);

//	public boolean insertLimitPolicy(Map<String, Object> policyInfo);
	
}
