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
	public ArrayList<Map<String, Object>> getStockList();
//
//	public ArrayList<Map<String, Object>> getStock(String sku_id);
//
//	public boolean insertLimitPolicy(Map<String, Object> policyInfo);
}
