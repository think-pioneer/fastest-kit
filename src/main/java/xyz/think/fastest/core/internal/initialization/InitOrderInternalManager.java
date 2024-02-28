package xyz.think.fastest.core.internal.initialization;

import xyz.think.fastest.core.enhance.Initializable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内部order管理器
 */
final class InitOrderInternalManager {
    private volatile static InitOrderInternalManager INSTANCE;

    private final ConcurrentHashMap<Class<? extends Initializable>, Integer> cache;
    private Integer order;
    private InitOrderInternalManager(){
        this.cache = new ConcurrentHashMap<>();
        this.order = Integer.MIN_VALUE;
    }

    public static InitOrderInternalManager getInstance() {
        if (INSTANCE == null) {
            synchronized (InitOrderInternalManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InitOrderInternalManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 消费一个order，消费完后自动做加法
     */
    public int consumer(Class<? extends Initializable> type){
        int order = cache.getOrDefault(type, this.order++);
        this.cache.put(type, order);
        return order;
    }
}
