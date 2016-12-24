package com.QAboard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;
 
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }
    
    public static void main(String[] args) {
        
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();
        
        //pv
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        
        //set timeout
        jedis.setex("hello2", 20, "world");
        
        //数值型的，瞬间变化很大
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));
        
        
        // print all
        print(3, jedis.keys("*"));
        
        
        //List lpush/rpush: push an element into the end/start of the list; 
        String listName = "list";
        jedis.del(listName);
        for(int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(4, jedis.lrange(listName, 0, 12));
        print(4, jedis.lrange(listName, 0, 1));
        
        print(5, jedis.llen(listName));
        print(6, jedis.lpop(listName));
        print(7, jedis.llen(listName));
        print(7, jedis.rpush(listName, "bb"));
        print(8, jedis.lindex(listName, 3));
        print(9, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a1", "aa"));
        print(10, jedis.lrange(listName, 0, 10));
        
        // hash
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "18618181818");
        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));
        print(16, jedis.hexists(userKey, "age"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        //if not exist,add it.  
        jedis.hsetnx(userKey, "school", "zju");
        jedis.hsetnx(userKey, "name", "yxy");
        print(19, jedis.hgetAll(userKey));
        
        //set
        String setA = "setA";
        String setB = "setB";
        
        for (int i = 0; i < 10; i++) {
            jedis.sadd(setA, String.valueOf(i));
            jedis.sadd(setB, String.valueOf(i*i));
        }
        
        print(20, jedis.smembers(setA));
        print(21, jedis.sunion(setA, setB));
        print(22, jedis.sdiff(setA, setB));
        print(24, jedis.sinter(setA, setB));
        print(25, jedis.sismember(setA, "1"));
        print(26, jedis.sismember(setA, "10"));
        jedis.srem(setA, "5");
        print(27, jedis.smembers(setA));
        jedis.smove(setB, setA, "25");
        print(28, jedis.smembers(setA));
        //get the size of the set
        print(29, jedis.scard(setA));
        
        
        //sorted set
        
        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(40, jedis.zlexcount(setKey, "-", "+"));
        print(41, jedis.zlexcount(setKey, "(b", "[d"));
        print(42, jedis.zlexcount(setKey, "[b", "[d"));
        
        jedis.zrem(setKey, "b");
        print(43, jedis.zrange(setKey, 0, 10));
        jedis.zremrangeByLex(setKey, "(c", "+");
        print(44, jedis.zrange(setKey, 0 ,2));
  
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
        
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch(Exception e) {
            logger.error("fail" +e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        return 0;
     }
    
    public long srem(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, member);
        } catch(Exception e) {
            logger.error("fail" +e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        return 0;
     }
    
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch(Exception e) {
            logger.error("fail" +e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    
    public boolean sismember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        } catch(Exception e) {
            logger.error("fail" +e.getMessage());
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        return false;
    }
}
