package com.itheima.leyou.service;

import java.util.Map;

public interface IStorageService {
	
	/**  
	 * @Describe: 查询某商品的库存
	 * @author LIN
	 * @date 2020-02-10 01:45:13 
	 */
    public Map<String, Object> getStockStorage(String sku_id);
    
    /**  
	 * @Describe: 增加或减少某商品的库存量
	 * @author LIN
	 * @date 2020-02-10 01:57:46 
	 */
    public Map<String, Object> insertStorage(String sku_id, double in_quanty, double out_quanty);
}
