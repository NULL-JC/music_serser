package com.haut.music.storage;


import com.haut.music.base.BaseDomain;
import com.haut.music.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCacheStorage<T extends BaseDomain> {
    protected abstract String getCacheKey();

    protected String genUUID() {
        return UUIDUtil.genUUID();
    }

    @Autowired
    private CacheManager cacheManager;

    public T save(T t) {
        if (StringUtils.isEmpty(t.getId())) {
            t.setId(genUUID());
        }
        Cache cache = cacheManager.getCache(getCacheKey());
        cache.put(t.getId(), t);
        return t;
    }

    public Boolean delete(String id) {
        Cache cache = cacheManager.getCache(getCacheKey());
        cache.evict(id);
        return true;
    }


    public T get(String id) {
        Cache cache = cacheManager.getCache(getCacheKey());
        return (T) cache.get(id, Object.class);
    }

    public List<T> listAll() {
        Cache cache = cacheManager.getCache(getCacheKey());
        if (cache instanceof EhCacheCache) {
            List<String> keys = ((EhCacheCache) cache).getNativeCache().getKeys();
            if (keys != null && !keys.isEmpty()) {
                List<T> result = new ArrayList<>();
                for (String key : keys) {
                    result.add((T) cache.get(key, Object.class));
                }
                return result;
            }
        }
        return null;
    }


}
