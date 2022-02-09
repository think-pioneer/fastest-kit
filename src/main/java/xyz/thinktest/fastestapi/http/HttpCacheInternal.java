package xyz.thinktest.fastestapi.http;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public enum HttpCacheInternal {
    INSTANCE;
    private final Map<String, Object> cache;
    HttpCacheInternal(){
        cache = new HashMap<>();
    }

    public void set(String key, Object value){
        this.cache.put(key, value);
    }

    public <T> T get(String key){
        return (T) this.cache.get(key);
    }
}
