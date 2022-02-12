package xyz.thinktest.fastestapi.common.cache;

/**
 * @Date: 2020/10/17
 */
@SuppressWarnings("unchecked")
public class CacheManager {
    private static final Manager manager = Manager.CACHE_MANAGER;

    /**
     * put a non expired k v
     * @param key cache key
     * @param value cache value
     */
    public static void put(Object key, Object value){
        manager.put(key, value);
    }

    /**
     * put an expired k v
     * @param key cache key
     * @param value cache value
     * @param duration duration in second
     */
    public static void put(String key, Object value, int duration){
        manager.put(key, value, duration);
    }

    /**
     * the value is suitable to judge whether the value is expired
     * @param key  cache key
     * @return value or null (if the key does not exist or expires)
     */
    public static <T> T get(String key){
        return (T) manager.get(key);
    }
}
