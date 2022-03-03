package com.haut.music.cache;

import com.alibaba.fastjson.JSON;
import com.haut.music.cache.vo.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisServiceImpl implements RedisService {

    /**
     * 获取redis连接
     */
    @Autowired
    private JedisPool jedisPool;

    @Override
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String strValue = jedis.get(realKey);
            //将jason字符串转换成对应对象
            T objValue = stringToBean(strValue, clazz);
            return objValue;
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //将对象转换成jason字符串
            String strValue = beanToString(value);
            if(strValue == null || strValue.length()==0)
                return false;

            String realKey = prefix.getPrefix() + key;
            int expire = prefix.expireSeconds();
            if(expire<=0) {
                jedis.set(realKey, strValue);
            }
            else {
                jedis.setex(realKey, expire, strValue);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }



    @Override
    public boolean exists(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long incr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long decr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            Long del = jedis.del(realKey);
            return del > 0;
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 根据传入class参数，将字符串转化成对象
     * @param strValue json字符串
     * @param clazz 类型
     * @param <T> 类型参数
     * @return 对象
     */
    private <T> T stringToBean(String strValue, Class<T> clazz) {
        if(strValue == null || strValue.length() == 0 || clazz==null)
            return null;
        if(clazz==int.class || clazz == Integer.class)
            return (T) Integer.valueOf(strValue);
        else if(clazz == Long.class || clazz == long.class)
            return (T) Long.valueOf(strValue);
        else if(clazz == String.class)
            return (T) strValue;
        else
            return JSON.toJavaObject(JSON.parseObject(strValue), clazz);
    }

    /**
     * 将对象转换成jaon字符串
     * @param value 对象
     * @param <T> 对象类型
     * @return json字符串
     */
    private <T> String beanToString(T value) {
        if(value == null)
            return null;
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class)
            return "" + value;
        else if (clazz == long.class || clazz == Long.class)
            return "" + value;
        else if (clazz == String.class)
            return (String) value;
            /*然后对Object类型的对象处理*/
        else
            return JSON.toJSONString(value);
    }

    /**
     * 将redis连接对象归还到redis连接池
     * @param jedis jedis对象
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }
}
