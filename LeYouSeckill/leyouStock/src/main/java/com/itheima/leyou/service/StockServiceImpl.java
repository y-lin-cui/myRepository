package com.itheima.leyou.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.leyou.dao.IStockDao;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author:ylin
 * @tips:
 * @version 1
 * @Date:上午3:47:30
 */
@Service
public class StockServiceImpl implements IStockService {

	@Autowired
	private IStockDao iStockDao;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 获取商品列表，主要为了展示商品列表页
	 * 
	 * @return list，包含一个商品的map
	 */
	public Map<String, Object> getStockList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 1、取IstockDao的方法
		ArrayList<Map<String, Object>> list = iStockDao.getStockList();

		// 2、如果没取出数据，返回错误信息
		if (list == null || list.size() == 0) {
			resultMap.put("result", false);
			resultMap.put("msg", "我们也不知道为啥没取出数据！");
			return resultMap;
		}

		// 3、从redis中获取秒杀政策，（如秒杀政策已过期，redis会自动清除）
		resultMap = getLimitPolicy(list);

		// 4、返回信息
		resultMap.put("sku_list", list);

		return resultMap;
	}
	
	/**
	 * @Describe: 获取商品详细信息
	 * @author LIN
	 * @date 2020-02-08 03:34:38
	 */
	public Map<String, Object> getStock(String sku_id){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //1、判断前端传入的数据
        if (sku_id==null||sku_id.equals("")){
            resultMap.put("result", false);
            resultMap.put("msg", "前端传过来的什么东东？");
            return resultMap;
        }

        //2、取stockDao的方法
        ArrayList<Map<String, Object>> list = iStockDao.getStock(sku_id);

        //3、判断如果没取出来
        if (list==null||list.size()==0){
            resultMap.put("result", false);
            resultMap.put("msg", "数据库咋还没取出来！");
            return resultMap;
        }

        //3、取redis取政策
        resultMap = getLimitPolicy(list);

        //4、返回正常信息
        resultMap.put("sku", list);

        return resultMap;
    }

	
	/**  
	 * @Describe: 为商品遍历秒杀政策
	 * @author LIN
	 * @date 2020-02-08 03:28:14 
	 */
	private Map<String, Object> getLimitPolicy(ArrayList<Map<String, Object>> list) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		for (Map<String, Object> skuMap : list) {
			String policy = stringRedisTemplate.opsForValue().get("LIMIT_POLICY_" + skuMap.get("sku_id").toString());
			// 3.1 判断如果有政策，给list赋值
			if (policy != null && !policy.equals("")) {
				// 3.2 判断结束时间>=当前时间，并且当前时间>=开始时间
				Map<String, Object> policyInfo = JSONObject.parseObject(policy, Map.class);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String now = restTemplate.getForObject("http://leyou-time-server/getTime", String.class);

				try {
					Date end_time = simpleDateFormat.parse(policyInfo.get("end_time").toString());
					Date begin_time = simpleDateFormat.parse(policyInfo.get("begin_time").toString());
					Date now_time = simpleDateFormat.parse(now);

					if (begin_time.getTime() <= now_time.getTime() && now_time.getTime() <= end_time.getTime()) {
						skuMap.put("limitPrice", policyInfo.get("price"));
						skuMap.put("limitQuanty", policyInfo.get("quanty"));
						skuMap.put("limitBeginTime", policyInfo.get("begin_time"));
						skuMap.put("limitEndTime", policyInfo.get("end_time"));
						skuMap.put("nowTime", now);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		}

		resultMap.put("result", true);
		resultMap.put("msg", "");
		return resultMap;
	}
	
	/**  
	 * @Describe: 新增秒杀政策，并写入缓存
	 * @author LIN
	 * @date 2020-02-08 09:41:17 
	 */
	@Transactional
	 public Map<String, Object> insertLimitPolicy(Map<String, Object> policyInfo){
	        Map<String, Object> resultMap = new HashMap<String, Object>();

	        //1、判断传入的参数
	        if (policyInfo==null||policyInfo.isEmpty()){
	            resultMap.put("result", false);
	            resultMap.put("msg", "前端传过来的什么东东？");
	            return resultMap;
	        }

	        //2、取stockdao的方法，新增政策
	        boolean result = iStockDao.insertLimitPolicy(policyInfo);

	        if (!result){
	            resultMap.put("result", false);
	            resultMap.put("msg", "数据库咋还写失败了！");
	            return resultMap;
	        }

	        //3、写入redis
	        long diff = 0;
	        //3.1、写入政策，key: LIMIT_POLICY_{sku_id}， value: policyInfo， 有效期：结束时间-当前时间

	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        String now = restTemplate.getForObject("http://leyou-time-server/getTime", String.class);

	        try {
	            Date end_time = simpleDateFormat.parse(policyInfo.get("end_time").toString());
	            Date now_time = simpleDateFormat.parse(now);

	            diff = (end_time.getTime()-now_time.getTime())/1000;

	            if (diff<0){
	                resultMap.put("result", false);
	                resultMap.put("msg", "结束时间不能小于当前时间！");
	                return resultMap;
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        String policy = JSON.toJSONString(policyInfo);
	        stringRedisTemplate.opsForValue().set("LIMIT_POLICY_"+policyInfo.get("sku_id").toString(), policy, diff, TimeUnit.SECONDS);

	        //3.2、获取商品的政策
	        ArrayList<Map<String, Object>> list = iStockDao.getStock(policyInfo.get("sku_id").toString());
	        String sku = JSON.toJSONString(list.get(0));
	        
	        //3.3、存入Redis
	        /**  
			 * opsForValue().set(K key, V value, long timeout, TimeUnit unit)：
			 * key ：字段key
			 * value：key对应的值
		     * timeout：超时时间
		     * TimeUnit：超时时间单位
			 */
	        stringRedisTemplate.opsForValue().set("SKU_"+policyInfo.get("sku_id").toString(), sku, diff, TimeUnit.SECONDS);

	        //4、返回正常信息
	        resultMap.put("result", true);
	        resultMap.put("msg", "");
	        return resultMap;
	    }
}
