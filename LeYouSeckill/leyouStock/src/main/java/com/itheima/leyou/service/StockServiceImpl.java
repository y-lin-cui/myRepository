package com.itheima.leyou.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.leyou.dao.IStockDao;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
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

	public Map<String, Object> getStockList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 1、取IstockDao的方法
		ArrayList<Map<String, Object>> list = iStockDao.getStockList();
		
		  //2、如果没取出数据，返回错误信息
        if (list==null||list.size()==0){
            resultMap.put("result", false);
            resultMap.put("msg", "我们也不知道为啥没取出数据！");
            return resultMap;
        }
        
        //3、取redis取政策
        resultMap = getLimitPolicy(list);
		
		return resultMap;
	}
	
	 private Map<String, Object> getLimitPolicy(ArrayList<Map<String, Object>> list){
	        Map<String, Object> resultMap = new HashMap<String, Object>();

	        for (Map<String, Object> skuMap: list){
	            String policy = stringRedisTemplate.opsForValue().get("LIMIT_POLICY_"+skuMap.get("sku_id").toString());
	            //3.1 判断如果有政策，给list赋值
	            if (policy!=null&&!policy.equals("")){
	                //3.2 判断结束时间>=当前时间，并且当前时间>=开始时间
	                Map<String, Object> policyInfo = JSONObject.parseObject(policy, Map.class);

	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                String now = restTemplate.getForObject("http://leyou-time-server/getTime", String.class);

	                try {
	                    Date end_time = simpleDateFormat.parse(policyInfo.get("end_time").toString());
	                    Date begin_time = simpleDateFormat.parse(policyInfo.get("begin_time").toString());
	                    Date now_time = simpleDateFormat.parse(now);

	                    if (begin_time.getTime()<=now_time.getTime()&&now_time.getTime()<=end_time.getTime()){
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


	        resultMap.put("result",true);
	        resultMap.put("msg", "");
	        return resultMap;
	    }

}
