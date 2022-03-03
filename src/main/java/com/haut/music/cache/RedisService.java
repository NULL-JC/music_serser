package com.haut.music.cache;


import com.haut.music.cache.vo.KeyPrefix;

/**
 * redis 服务接口
 */
public interface RedisService {
    /**
     * redis的get操作，通过key存储在redis的对象
     * @param prefix key的前缀
     * @param key 传入key
     * @param clazz 存储在redis的对象类型
     * @param <T> 指定对象的类型
     * @return 存储redis的对象
     */
    <T> T get(KeyPrefix prefix, String key, Class<T> clazz);

    /**
     * redis的set操作
     * @param prefix key的前缀
     * @param key 传入key
     * @param value 值
     * @param <T> 类型
     * @return 操作成功返回true
     */
    <T> boolean set(KeyPrefix prefix, String key, T value);

    /**
     * 判断key是否存在
     * @param keyPrefix
     * @param key
     * @return
     */
    boolean exists(KeyPrefix keyPrefix, String key);

    /**
     * 自增
     * @param keyPrefix
     * @param key
     * @return
     */
    long incr(KeyPrefix keyPrefix, String key);

    /**
     * 自减
     * @param keyPrefix
     * @param key
     * @return
     */
    long decr(KeyPrefix keyPrefix, String key);

    /**
     * 删除缓存数据
     * @param prefix
     * @param key
     * @return
     */
    boolean delete(KeyPrefix prefix, String key);
}
