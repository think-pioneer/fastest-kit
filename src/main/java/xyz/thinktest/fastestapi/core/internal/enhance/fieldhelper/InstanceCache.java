package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2021/10/31
 */
enum InstanceCache {

    CACHE;

    private final ConcurrentHashMap<Integer, Object> cache;
    InstanceCache(){
        this.cache = new ConcurrentHashMap<>();
    }

    public void put(Integer key, Object value){
        this.cache.put(key, value);
    }

    public Object get(Integer key){
        return this.cache.get(key);
    }

}
