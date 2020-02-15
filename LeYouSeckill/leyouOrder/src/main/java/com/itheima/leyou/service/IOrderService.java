package com.itheima.leyou.service;

import java.util.Map;

public interface IOrderService {

	/**
	 * @Describe: 写入订单，主要应用于队列写入订单
	 * @author LIN
	 * @date 2020-02-16 02:01:02
	 */
	public Map<String, Object> insertOrder(Map<String, Object> orderInfo);

	/**
	 * @Describe: 更新订单 主要应用于队列更新订单状态的方法
	 * @author LIN
	 * @date 2020-02-16 02:49:09
	 */
	public Map<String, Object> updateOrderStatus(String order_id);

	/**
	 * @Describe: 订单查询 查询订单详情，用于会员中心--我的订单查询
	 * @author LIN
	 * @date 2020-02-16 02:12:10
	 */
	public Map<String, Object> getOrder(String order_id);
	
	/**
	 * @Describe: 创建订单
	 * @author LIN
	 * @date 2020-02-16 01:44:44
	 */
	public Map<String, Object> createOrder(String sku_id, String user_id);

	/**
	 * @Describe: 订单支付 主要用于前端支付页面调用，并写入订单状态更新队列
	 * @author LIN
	 * @date 2020-02-16 02:56:47
	 */
	public Map<String, Object> payOrder(String order_id, String sku_id);

}
