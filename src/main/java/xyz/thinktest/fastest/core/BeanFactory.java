package xyz.thinktest.fastest.core;

import xyz.thinktest.fastest.common.exceptions.FastestBasicException;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * @Date: 2022/1/1
 */
public final class BeanFactory {
    public static <T> T newInstance(Class<T> clazz){
        try{
            return clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new FastestBasicException(ObjectUtil.format("create {} instance error", clazz.getName()));
        }
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        try{
            return clazz.getDeclaredConstructor(argumentTypes).newInstance(arguments);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new FastestBasicException(ObjectUtil.format("create {} instance error", clazz.getName()));
        }
    }
}
