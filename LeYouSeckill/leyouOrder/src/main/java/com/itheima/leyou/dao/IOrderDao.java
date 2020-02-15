package com.itheima.leyou.dao;

import java.util.ArrayList;
import java.util.Map;

public interface IOrderDao {
	
	/**
	 * @Describe: 写入订单，主要应用于队列写入订单
	 * @author LIN
	 * @date 2020-02-16 02:01:02
	 */
    public Map<String,Object> insertOrder(Map<String, Object> orderInfo);
    
    /**  
   	 * @Describe: 更新订单 主要应用于队列更新订单状态的方法
   	 * @author LIN
   	 * @date 2020-02-16 02:49:09 
   	 */
    public boolean updateOrderStatus(String order_id);
    
    /**  
	 * @Describe: 订单查询  查询订单详情，用于会员中心--我的订单查询
	 * @author LIN
	 * @date 2020-02-16 02:12:10 
	 */
    public ArrayList<Map<String, Object>> getOrder(String order_id);
    
}
