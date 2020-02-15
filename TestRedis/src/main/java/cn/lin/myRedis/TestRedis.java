package cn.lin.myRedis;

import java.util.LinkedList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class TestRedis {
	public static ShardedJedisPool pool;
	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100);
		config.setMaxIdle(50);
		config.setMaxWaitMillis(3000);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		// 集群
		JedisShardInfo jedisShardInfo1 = new JedisShardInfo("39.96.38.153", 6379);
		jedisShardInfo1.setPassword("first");
		List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
		list.add(jedisShardInfo1);
		pool = new ShardedJedisPool(config, list);
	}

	public static void main(String[] args) {
		ShardedJedis jedis = pool.getResource();
//		jedis.set("name", "呵呵");
		String name = jedis.get("name");
		System.out.println("name的值：" + name);
	}

}
