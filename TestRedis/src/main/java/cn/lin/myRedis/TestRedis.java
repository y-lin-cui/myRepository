package cn.lin.myRedis;

import java.util.List;

import redis.clients.jedis.Jedis;

public class TestRedis {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("39.96.38.153");
		System.out.println(jedis.getClient().getPort());
		System.out.println("连接Redis服务器成功");
//		jedis.set("name", "呵呵");
		String name = jedis.get("name");
		System.out.println("name的值："+name);
		
//		List<String> list = jedis.mget("name","age");
//		for (int i = 0; i < list.size(); i++) {
//			String string = list.get(i);
//			System.out.println(string);
//		}
	}
}
