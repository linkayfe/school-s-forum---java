package com.gcuedu.gcuforum.util;

import com.gcuedu.gcuforum.config.JedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
public class RedisUtil {
    private static JedisPool jedisPool;
    @Autowired
    private JedisConfig jedisConfig;

    @PostConstruct
    public void init(){
        jedisPool = jedisConfig.jedisPoolFactory();
    }

    public static byte[] get(String key,int indexDb){
        Jedis jedis = null;
        byte[] value = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            value = jedis.get(key.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            assert jedis != null;
            jedis.close();
        }
        return value;
    }

    public static void setex(String key,int indexDb,int ttl,byte[] obj){
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            jedis.setex(bytes,ttl,obj);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            assert jedis!=null;
            jedis.close();
        }
    }

    public static boolean exists(String key,int indexDb){
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        Jedis jedis = null;
        boolean exist = false;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            exist = jedis.exists(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            assert jedis!=null;
            jedis.close();
        }
        return exist;
    }

    public static long ttl(String key,int indexDb){
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        Jedis jedis = null;
        long ttl = 0;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            ttl = jedis.ttl(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            assert jedis!=null;
            jedis.close();
        }
        return ttl;
    }

    public static void setTtl(String key,int indexDb,int ttl){
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            jedis.expire(bytes,ttl);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            assert jedis!=null;
            jedis.close();
        }
    }

    public static void delKey(String key, int indexDb){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            assert jedis!=null;
            jedis.close();
        }
    }
}
