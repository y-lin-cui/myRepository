package com.itheima.leyou.dao;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author:ylin
 * @tips:
 * @version 1
 * @Date:上午3:46:34
 */
@Repository
public class StockDaoImpl implements IStockDao {

	/*
	 * 引入JDBC连接对象
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取商品列表，主要为了展示商品列表页
	 * 
	 * @return list，包含一个商品的map
	 */
	public ArrayList<Map<String, Object>> getStockList() {
		// 1、创建语句
		String sql = "select id as sku_id, title, images, stock, price, indexes, own_spec " + "from tb_sku";

		// 2、执行语句
		ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) jdbcTemplate.queryForList(sql);

		// 3、返回信息
		return list;
	}

	/**
	 * @Describe: 获取商品详细信息
	 * @author LIN
	 * @date 2020-02-08 03:34:38
	 */
	@Override
	public ArrayList<Map<String, Object>> getStock(String sku_id) {
		// TODO Auto-generated method stub
		// 1、创建语句
		String sql = "select tb_sku.spu_id, tb_sku.title, tb_sku.images, tb_sku.stock, tb_sku.price, tb_sku.indexes, " +
                "tb_sku.own_spec, tb_sku.enable, tb_sku.create_time, tb_sku.update_time,tb_spu_detail.description," +
                "tb_sku.id AS sku_id,tb_spu_detail.special_spec " +
                "from tb_sku " +
                "INNER JOIN tb_spu_detail ON tb_spu_detail.spu_id=tb_sku.spu_id " +
                "where tb_sku.id = ?";

		System.out.println("sql的值是："+sql);
		// 2、执行语句
		ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) jdbcTemplate.queryForList(sql, sku_id);

		// 3、返回信息
		return list;
	}
	
	/**  
	 * @Describe: 新增秒杀政策，并写入缓存
	 * @author LIN
	 * @date 2020-02-08 09:41:17 
	 */
	public boolean insertLimitPolicy(Map<String, Object> policyInfo){
		 //1、创建语句
        String sql = "insert into tb_limit_policy (sku_id, quanty, price, begin_time, end_time) " +
                "Values (?, ?, ?, ?, ?)";
        //2、执行
        boolean result = jdbcTemplate.update(sql, policyInfo.get("sku_id"), policyInfo.get("quanty"),
                policyInfo.get("price"), policyInfo.get("begin_time"), policyInfo.get("end_time"))==1;

        //3、返回
        return result;
	}

}
