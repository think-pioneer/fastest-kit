package xyz.think.fastest.common.cache;

/**
 * 缓存元信息。
 * @author: aruba
 * @date: 2022-02-12
 */
class Cache {
    static final long FOREVER_FLAG = -1;
    /**
     * 缓存对象
     */
    private final Object data;
    /**
     * 缓存时间戳
     */
    private final long timestamp = System.currentTimeMillis();
    /**
     * 过期时间。
     */
    private final long duration;  // -1表示永不过期


    public Cache(Object data, int duration) {
        this.data = data;
        this.duration = duration;
    }

    public Cache(Object data) {
        this.data = data;
        this.duration = FOREVER_FLAG;
    }

    public Object getData() {
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
