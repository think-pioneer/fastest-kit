package org.fastest.core.internal.enhance.fieldhelper;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2021/10/31
 */
class InstanceCache extends ConcurrentHashMap<Integer, Object> {
    private volatile static InstanceCache cache = null;
    private InstanceCache(){}

    public static InstanceCache getInstance(){
        if(Objects.isNull(cache)){
            synchronized (InstanceCache.class){
                if(Objects.isNull(cache)){
                    cache = new InstanceCache();
                }
            }
        }
        return cache;
    }
}
