package xyz.thinktest.fastestapi.common.cache;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2020/10/17
 */
@SuppressWarnings("unchecked")
public class CacheManager<T> {
    private final ConcurrentMap<Object,Cache<T> > map = new ConcurrentHashMap<>();
    private static volatile CacheManager instance = null;
    private CacheManager(){}

    public static <T> CacheManager<T> getInstance(){
        if(Objects.isNull(instance)){
            synchronized (CacheManager.class){
                if(Objects.isNull(instance)){
                    instance = new CacheManager<>();
                }
            }
        }

        return instance;
    }

    /**
     * put a non expired k v
     * @param key cache key
     * @param value cache value
     */
    public void put(Object key, T value){
        map.put(key, new Cache<>(value));
    }

    /**
     * put an expired k v
     * @param key cache key
     * @param value cache value
     * @param duration duration in second
     */
    public void put(Object key, T value, int duration){
        map.put(key, new Cache<>(value, duration));
    }

    /**
     * the value is suitable to judge whether the value is expired
     * @param key  cache key
     * @return value or null (if the key does not exist or expires)
     */
    public T get(Object key){
        Cache<T> v = map.get(key);
        if(Objects.isNull(v)){
            return null;
        }
        if(expired(v)){
            map.remove(key);
            return null;
        }
        return v.getData();
    }

    /**
     * check expired
     * @param v cache value
     * @return whether to expire
     */
    private boolean expired(Cache<T> v){
        Objects.requireNonNull(v);
        return v.getDuration() != Cache.FOREVER_FLAG && (System.currentTimeMillis() > v.getDuration() + v.getTimestamp());
    }

    private static class Cache<T> {
        static final long FOREVER_FLAG = -1;

        private final T data;
        private final long timestamp = System.currentTimeMillis();
        private final long duration;  // -1表示永不过期


        public Cache(T data, int duration){
            this.data = data;
            this.duration = TimeUnit.SECONDS.toMillis(duration);
        }

        public Cache(T data) {
            this.data = data;
            this.duration = FOREVER_FLAG;
        }

        public T getData() {
            return data;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getDuration() {
            return duration;
        }

        @Override
        public String toString() {
            return "Cache{" +
                    "data=" + data +
                    ", timestamp=" + timestamp +
                    ", duration=" + duration +
                    '}';
        }
    }
}
