package xyz.thinktest.fastestapi.http;

import java.util.HashMap;
import java.util.Map;

public enum HttpCacheInternal {
    INSTANCE;
    private final Map<String, Object> cache;
    HttpCacheInternal(){
        cache = new HashMap<>();
    }

    public void set(String key, Object value){
        this.cache.put(key, value);
    }

    public Object get(String key){
        return this.cache.get(key);
    }
}
