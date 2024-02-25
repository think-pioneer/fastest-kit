package xyz.thinktest.fastestapi.core.internal.scanner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存字段注解元数据
 * @author: aruba
 * @date: 2022-01-27
 */
public enum FieldAnnotationProcessCache {
    INSTANCE;
    private final ConcurrentHashMap<String, FieldAnnotationProcessMeta> map;
    FieldAnnotationProcessCache(){
        this.map = new ConcurrentHashMap<>();
    }

    public FieldAnnotationProcessMeta get(Field field){
        return this.map.get(buildKey(field));
    }

    public void put(Field field, FieldAnnotationProcessMeta entity){
        this.map.put(buildKey(field), entity);
    }

    public Collection<FieldAnnotationProcessMeta> allValue(){
        return this.map.values();
    }

    private String buildKey(Field field){
        return field.getDeclaringClass().getCanonicalName() + "-" +
                field.getType().getCanonicalName() + "-" +
                field.getName();
    }
}
