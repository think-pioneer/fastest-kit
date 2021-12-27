package xyz.thinktest.fastest.core;

import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;

/**
 * @Date: 2021/12/7
 */
public final class ApplicationBean {
    private ApplicationBean(){}

    public static Object enhance(Class<?> clazz, Class<?> callbackType){
        return EnhanceFactory.enhance(clazz, callbackType);
    }

    public static Object enhance(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments, Class<?> callbackType){
        return EnhanceFactory.enhance(clazz, argumentTypes, arguments, callbackType);
    }

    public static Object origin(Class<?> clazz){
        return EnhanceFactory.origin(clazz);
    }

    public static Object origin(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments){
        return EnhanceFactory.origin(clazz, argumentTypes, arguments);
    }
}