package xyz.think.fastest.common.cache;

import xyz.think.fastest.utils.files.PropertyUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器
 * @author: aruba
 * @date: 2022-02-12
 */
enum Manager {
    CACHE_MANAGER;
    final ConcurrentMap<Object,Cache> cacheMap;
    Manager(){
        long period = PropertyUtil.getOrDefault("fastest.cache.expired.period", 10000);
        this.cacheMap = new ConcurrentHashMap<>();
        // 创建一个定时任务，定时清理过期缓存
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<Object, Cache> cacheMap = Manager.CACHE_MANAGER.cacheMap;
                cacheMap.keySet().forEach(key -> {
                    if(expired(cacheMap.get(key))){
                        cacheMap.remove(key);
                    }
                });
            }
        }, 10000, period);
    }
    /**
     * put a non expired k v
     * @param key cache key
     * @param value cache value
     */
    public synchronized void put(Object key, Object value){
        this.cacheMap.put(key, new Cache(value));
    }

    /**
     * put an expired k v
     * @param key cache key
     * @param value cache value
     * @param duration duration in second
     */
    public synchronized void put(Object key, Object value, int duration){
        this.cacheMap.put(key, new Cache(value, duration));
    }

    /**
     * the value is suitable to judge whether the value is expired
     * @param key  cache key
     * @return value or null (if the key does not exist or expires)
     */
    public synchronized Object get(Object key){
        Cache v = this.cacheMap.get(key);
        if(Objects.isNull(v)){
            return null;
        }
        if(expired(v)){
            this.cacheMap.remove(key);
            return null;
        }
        return v.getData();
    }

    /**
     * check expired
     * @param v cache value
     * @return whether to expire
     */
    private boolean expired(Cache v){
        Objects.requireNonNull(v);
        return v.getDuration() != Cache.FOREVER_FLAG && (System.currentTimeMillis() > v.getDuration() + v.getTimestamp());
    }

    /**
     * Manually sort out the cache and clean up the expired cache.
     * @return Expired caches
     */
    public List<Cache> collating() {
        List<Cache> rubbish = new ArrayList<>();
        cacheMap.keySet().forEach(key -> {
            if(expired(cacheMap.get(key))){
                rubbish.add(cacheMap.remove(key));
            }
        });
        return rubbish;
    }
}
