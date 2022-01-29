package xyz.thinktest.fastestapi.core.internal.tool;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: aruba
 * @date: 2022-01-25
 */
@SuppressWarnings("unchecked")
public enum MethodAnnotationProcessCache {
    INSTANCE;

    private final ConcurrentHashMap<String, MethodAnnotationProcessEntity> map;
    MethodAnnotationProcessCache(){
        this.map = new ConcurrentHashMap<>();
    }

    public MethodAnnotationProcessEntity get(Method method){
        return this.map.get(buildKey(method));
    }

    public void put(Method method, MethodAnnotationProcessEntity entity){
        this.map.put(buildKey(method), entity);
    }

    public Collection<MethodAnnotationProcessEntity> allValue(){
        return this.map.values();
    }

    private String buildKey(Method method){
        StringBuilder key = new StringBuilder();
        key.append(method.getDeclaringClass().getCanonicalName()).append("-");
        key.append(method.getName()).append("-");
        for(Parameter parameter : method.getParameters()){
            key.append(parameter.getType().getCanonicalName()).append("-");
        }
        key.append(method.getReturnType().getCanonicalName());
        return key.toString();
    }
}
