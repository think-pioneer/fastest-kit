package xyz.thinktest.fastestapi.core.internal.scanner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: aruba
 * @date: 2022-01-27
 */
public enum FieldAnnotationProcessCache {
    INSTANCE;
    private final ConcurrentHashMap<String, FieldAnnotationProcessEntity> map;
    FieldAnnotationProcessCache(){
        this.map = new ConcurrentHashMap<>();
    }

    public FieldAnnotationProcessEntity get(Field field){
        return this.map.get(buildKey(field));
    }

    public void put(Field field, FieldAnnotationProcessEntity entity){
        this.map.put(buildKey(field), entity);
    }

    public Collection<FieldAnnotationProcessEntity> allValue(){
        return this.map.values();
    }

    private String buildKey(Field field){
        return field.getDeclaringClass().getCanonicalName() + "-" +
                field.getType().getCanonicalName() + "-" +
                field.getName();
    }
}
