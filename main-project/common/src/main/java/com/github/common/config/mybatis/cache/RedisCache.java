package com.github.common.config.mybatis.cache;

import com.github.common.tool.ApplicationContextHolder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//若同时与devtool使用需做额外配置，否则反序列化会因classloader不同导致异常
public class RedisCache implements Cache {
    private final Integer EXPIRE_MINUTES = 6 * 60;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;
    private static RedisTemplate<Serializable, Serializable> redisTemplate;

    public RedisCache(String id) {
        this.id = id;
    }

    private void initRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextHolder.getBean("DBRedisTemplate",RedisTemplate.class);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        initRedisTemplate();
        redisTemplate.boundHashOps(getId()).put(key, value);
        redisTemplate.expire(getId(), EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public Object getObject(Object key) {
        initRedisTemplate();
        redisTemplate.expire(getId(), EXPIRE_MINUTES, TimeUnit.MINUTES);
        return redisTemplate.boundHashOps(getId()).get(key);
    }

    @Override
    public Object removeObject(Object key) {
        initRedisTemplate();
        return redisTemplate.boundHashOps(getId()).delete(key);
    }

    @Override
    public void clear() {
        initRedisTemplate();
        redisTemplate.delete(getId());
    }

    @Override
    public int getSize() {
        initRedisTemplate();
        Long size = redisTemplate.boundHashOps(getId()).size();
        return size == null ? 0 : size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        initRedisTemplate();
        return readWriteLock;
    }
    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }
        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }

}
