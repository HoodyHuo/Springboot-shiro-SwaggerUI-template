package tech.hoody.platform.shiro;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 由Redis 实现的 ShiroCache
 *
 * @param <K>
 * @param <V>
 */
public class RedisCache<K, V> implements Cache<K, V> {
    /**
     * 缓存的超时时间，单位为s
     */
    private long expireTime;

    /**
     * 通过构造方法注入该对象
     */
    private RedisTemplate<String, V> redisTemplate;

    private static final String SHIRO_REDIS_CACHE = "SHIRO_REDIS_CACHE";


    public RedisCache(Long expireTime, RedisTemplate<String, V> redisTemplate) {
        super();
        this.expireTime = expireTime == null ? 120 : expireTime;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public V get(K key) throws CacheException {
        V value = redisTemplate.opsForValue().get(SHIRO_REDIS_CACHE + key);
        return value;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        redisTemplate.opsForValue().set(SHIRO_REDIS_CACHE + key, value, this.expireTime, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        V v = redisTemplate.opsForValue().get(SHIRO_REDIS_CACHE + key);
        redisTemplate.opsForValue().getOperations().delete(SHIRO_REDIS_CACHE + key);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        Set<String> keys = redisTemplate.keys("*" + SHIRO_REDIS_CACHE + "*");
        redisTemplate.delete(keys);
    }

    @Override
    public int size() {
        Set<String> keys = redisTemplate.keys("*" + SHIRO_REDIS_CACHE + "*");
        return keys.size();
    }

    @Override
    public Set<K> keys() {
        Set<String> keys = redisTemplate.keys("*" + SHIRO_REDIS_CACHE + "*");
        return (Set<K>) keys;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
