package com.fh.shop.utils;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static JedisPool jedisPool;

    private RedisPool() {

    }

    private static void initPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMinIdle(500);
        jedisPoolConfig.setMaxIdle(500);
         jedisPool = new JedisPool(jedisPoolConfig,"192.168.179.133" ,7020 ,0,"123456");
    }

    static{
        initPool();
    }
        public static Jedis getResource(){
         return jedisPool.getResource();
    }
}
