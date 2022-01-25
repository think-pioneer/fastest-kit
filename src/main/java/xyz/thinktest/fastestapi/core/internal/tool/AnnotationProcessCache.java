package xyz.thinktest.fastestapi.core.internal.tool;

import xyz.thinktest.fastestapi.core.internal.enhance.AnnotationGardener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: aruba
 * @date: 2022-01-25
 */
public enum AnnotationProcessCache {
    INSTANCE;

    private final ConcurrentHashMap<Method, MethodAnnotationProcessEntity> map;
    AnnotationProcessCache(){
        this.map = new ConcurrentHashMap<>();
    }

    public MethodAnnotationProcessEntity get(Method method){
        return this.map.get(method);
    }

    public void put(Method method, MethodAnnotationProcessEntity entity){
        this.map.put(method, entity);
    }
}
