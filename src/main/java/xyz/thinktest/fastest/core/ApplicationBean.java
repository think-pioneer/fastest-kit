package xyz.thinktest.fastest.core;

import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;

/**
 * @Date: 2021/12/7
 */
public final class ApplicationBean {
    public static <T> T getEnhanceBean(Class<T> clazz){
        return EnhanceFactory.enhance(clazz);
    }

    public static <T> T getEnhanceBean(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        return EnhanceFactory.enhance(clazz, argumentTypes, arguments);
    }

    public static <T> T getOriginBean(Class<T> clazz){
        return EnhanceFactory.origin(clazz);
    }

    public static <T> T getOriginBean(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        return EnhanceFactory.origin(clazz, argumentTypes, arguments);
    }
}
