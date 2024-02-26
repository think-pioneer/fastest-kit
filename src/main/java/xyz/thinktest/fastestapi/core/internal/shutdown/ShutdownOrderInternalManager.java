package xyz.thinktest.fastestapi.core.internal.shutdown;

import xyz.thinktest.fastestapi.core.enhance.Shutdownable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内部使用
 */
final class ShutdownOrderInternalManager {
    private volatile static ShutdownOrderInternalManager INSTANCE;

    private final ConcurrentHashMap<Class<? extends Shutdownable>, Integer> cache;
    private Integer order;
    private ShutdownOrderInternalManager(){
        this.cache = new ConcurrentHashMap<>();
        this.order = Integer.MIN_VALUE;
    }

    public static ShutdownOrderInternalManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ShutdownOrderInternalManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShutdownOrderInternalManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 消费一个order，消费完后自动做加法
     */
    public int consumer(Class<? extends Shutdownable> type){
        int order = cache.getOrDefault(type, this.order++);
        this.cache.put(type, order);
        return order;
    }
}
