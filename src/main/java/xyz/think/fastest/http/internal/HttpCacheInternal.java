package xyz.think.fastest.http.internal;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public enum HttpCacheInternal {
    INSTANCE;
    private final Map<String, Class<?>> cache;
    HttpCacheInternal(){
        cache = new HashMap<>();
    }

    public void set(String key, Class<?> value){
        this.cache.put(key, value);
    }

    public <T> T get(String key){
        return (T) this.cache.get(key);
    }
}
