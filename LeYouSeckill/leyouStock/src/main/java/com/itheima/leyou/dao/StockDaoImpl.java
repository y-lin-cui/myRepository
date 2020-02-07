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
public class StockDaoImpl implements IStockDao{
	
	/*
	 * 引入JDBC连接对象
	 */
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	 /**
     * 获取商品列表，主要为了展示商品列表页
     * @return list，包含一个商品的map
     */
	public ArrayList<Map<String, Object>> getStockList(){
		 //1、创建语句
        String sql = "select id as sku_id, title, images, stock, price, indexes, own_spec " +
                "from tb_sku";

        //2、执行语句
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) jdbcTemplate.queryForList(sql);

        //3、返回信息
        return list;
	}
	
	
}
