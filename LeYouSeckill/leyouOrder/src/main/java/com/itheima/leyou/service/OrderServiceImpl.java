package com.itheima.leyou.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.leyou.dao.IOrderDao;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private IOrderDao iOrderDao;

	/**
	 * @Describe: 创建订单
	 * @author LIN
	 * @date 2020-02-16 01:44:44
	 */
	public Map<String, Object> createOrder(String sku_id, String user_id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 1、判断传入的参数
		if (sku_id == null || sku_id.equals("")) {
			resultMap.put("result", false);
			resultMap.put("msg", "前端传过来的数据有误！");
			return resultMap;
		}

		if (user_id == null || user_id.equals("")) {
			resultMap.put("result", false);
			resultMap.put("msg", "会员没有登录！");
			return resultMap;
		}

		String order_id = String.valueOf(System.currentTimeMillis());

		// 2、从redis取政策
		String key = "LIMIT_POLICY_" + sku_id;
		System.out.println("Redis中获取到的LIMIT_POLICY-key:"+key);
		String policy = stringRedisTemplate.opsForValue().get("LIMIT_POLICY_" + sku_id);

		if (policy != null && !policy.equals("")) {
			// 3、取出来的政策判断时间，结束时间>=当前时间，并且当前时间>=开始时间
			Map<String, Object> policyMap = JSONObject.parseObject(policy, Map.class);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String now = restTemplate.getForObject("http://leyou-time-server/getTime", String.class);

			try {
				Date begin_time = simpleDateFormat.parse(policyMap.get("begin_time").toString());
				Date end_time = simpleDateFormat.parse(policyMap.get("end_time").toString());
				Date now_time = simpleDateFormat.parse(now);

				if (begin_time.getTime() <= now_time.getTime() && now_time.getTime() <= end_time.getTime()) {

					// 库存
					long limitQuanty = Long.parseLong(policyMap.get("quanty").toString());

					/**
					 * @Describe: opsForValue().increment(key,num):以增量的方式
					 *            更新key对应的的vlue
					 * @author LIN
					 * @date 2020-02-16 01:46:02
					 */
					// 4、redis计数器
					if (stringRedisTemplate.opsForValue().increment("SKU_QUANTY_" + sku_id, 1) <= limitQuanty) {

						// 5、写入队列以及redis
						// tb_order: order_id, total_fee, actual_fee, post_fee,
						// payment_type, user_id, status, create_time
						// tb_order_detail: order_id, sku_id, num, title,
						// own_spec, price, image, create_time
						// tb_sku: sku_id, title, images, stock, price, indexes,
						// own_spec

						Map<String, Object> orderInfo = new HashMap<String, Object>();

						String sku = stringRedisTemplate.opsForValue().get("SKU_" + sku_id);
						Map<String, Object> skuMap = JSONObject.parseObject(sku, Map.class);

						orderInfo.put("order_id", order_id);
						orderInfo.put("total_fee", skuMap.get("price"));
						orderInfo.put("actual_fee", policyMap.get("price"));
						orderInfo.put("post_fee", 0);
						orderInfo.put("payment_type", 0);
						orderInfo.put("user_id", user_id);
						orderInfo.put("status", 1);
						orderInfo.put("create_time", now);

						orderInfo.put("sku_id", skuMap.get("sku_id"));
						orderInfo.put("num", 1);
						orderInfo.put("title", skuMap.get("title"));
						orderInfo.put("own_spec", skuMap.get("own_spec"));
						orderInfo.put("price", policyMap.get("price"));
						orderInfo.put("image", skuMap.get("images"));

						try {
							String order = JSON.toJSONString(orderInfo);
							amqpTemplate.convertAndSend("order_queue", order);
							stringRedisTemplate.opsForValue().set("ORDER_" + order_id, order);
						} catch (Exception e) {
							resultMap.put("result", false);
							resultMap.put("msg", "写入队列出错！");
							return resultMap;
						}
					} else {
						// 6、没通过计数器的，返回错误信息，提示商品已经售完
						resultMap.put("result", false);
						resultMap.put("msg", "商品已经售完，踢回去了3亿9！");
						return resultMap;
					}
				} else {
					// 8、时间判断有误，返回错误信息，提示活动已经过期
					resultMap.put("result", false);
					resultMap.put("msg", "活动已经过期！");
					return resultMap;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			// 7、没取出政策的时候，返回错误信息，提示活动已经过期
			// 取数据库
			resultMap.put("result", false);
			resultMap.put("msg", "活动已经过期！");
			return resultMap;
		}

		// 9、返回正常信息
		resultMap.put("order_id", order_id);
		resultMap.put("result", true);
		resultMap.put("msg", "订单已经生成！");
		return resultMap;
	}

	/**
	 * @Describe: 写入订单，主要应用于队列写入订单
	 * @author LIN
	 * @date 2020-02-16 02:01:02
	 */
	public Map<String, Object> insertOrder(Map<String, Object> orderInfo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = iOrderDao.insertOrder(orderInfo);
		return resultMap;
	}

	/**
	 * @Describe: 订单查询 查询订单详情，用于会员中心--我的订单查询
	 * @author LIN
	 * @date 2020-02-16 02:12:10
	 */
	public Map<String, Object> getOrder(String order_id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (order_id == null || order_id.equals("")) {
			resultMap.put("result", false);
			resultMap.put("msg", "参数传入有误！");
			return resultMap;
		}

		ArrayList<Map<String, Object>> list = iOrderDao.getOrder(order_id);
		resultMap.put("order", list);
		resultMap.put("result", true);
		resultMap.put("msg", "");
		return resultMap;
	}
	
	/**  
   	 * @Describe: 更新订单 主要应用于队列更新订单状态的方法
   	 * @author LIN
   	 * @date 2020-02-16 02:49:09 
   	 */
	public Map<String, Object> updateOrderStatus(String order_id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = iOrderDao.updateOrderStatus(order_id);
        if (!result){
            resultMap.put("result", false);
            resultMap.put("msg", "更新订单状态失败！");
            return resultMap;
        }

        resultMap.put("result", true);
        resultMap.put("msg", "");
        return resultMap;
    }
	
	/**  
	 * @Describe: 订单支付 主要用于前端支付页面调用，并写入订单状态更新队列
	 * @author LIN
	 * @date 2020-02-16 02:56:47 
	 */
	 public Map<String, Object> payOrder(String order_id, String sku_id){
	        Map<String, Object> resultMap = new HashMap<String, Object>();

	        //1、判断传入的参数
	        if (order_id==null||order_id.equals("")){
	            resultMap.put("result", false);
	            resultMap.put("msg", "前端传过来的数据有误！");
	            return resultMap;
	        }

	        if (sku_id==null||sku_id.equals("")){
	            resultMap.put("result", false);
	            resultMap.put("msg", "前端传过来的数据有误！");
	            return resultMap;
	        }

	        try {
	        	//放入订单队列
	            amqpTemplate.convertAndSend("order_status_queue", order_id);
	            //放入库存队列
	            amqpTemplate.convertAndSend("storage_queue", sku_id);
	        }catch (Exception e){
	            resultMap.put("result", false);
	            resultMap.put("msg", "队列没写成功！");
	            return resultMap;
	        }

	        resultMap.put("result", true);
	        resultMap.put("msg", "");
	        return resultMap;
	    }
}
