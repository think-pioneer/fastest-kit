package xyz.think.fastest.core.internal.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存方法注解元数据
 * @author: aruba
 * @date: 2022-01-25
 */
@SuppressWarnings("unchecked")
public enum MethodAnnotationProcessCache {
    INSTANCE;

    private final ConcurrentHashMap<String, MethodAnnotationProcessMeta> map;
    MethodAnnotationProcessCache(){
        this.map = new ConcurrentHashMap<>();
    }

    public MethodAnnotationProcessMeta get(Method method){
        return this.map.get(buildKey(method));
    }

    public void put(Method method, MethodAnnotationProcessMeta entity){
        this.map.put(buildKey(method), entity);
    }

    public Collection<MethodAnnotationProcessMeta> allValue(){
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
