package xyz.thinktest.fastest.core.internal.enhance;

import net.sf.cglib.proxy.Callback;
import xyz.thinktest.fastest.common.exceptions.EnhanceException;
import xyz.thinktest.fastest.common.exceptions.FastestBasicException;
import xyz.thinktest.fastest.core.BeanFactory;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @Date: 2020/10/29
 */
public final class EnhanceFactory {
    private EnhanceFactory(){}

    public static <T> T origin(Class<T> clazz){
        return BeanFactory.newInstance(clazz);
    }

    public static <T> T origin(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        return BeanFactory.newInstance(clazz, argumentTypes, arguments);
    }

    public static <T> T enhance(Class<T> clazz){
        return enhance(clazz, null, null, EasyHandler.class);
    }

    public static <T> T enhance(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        return enhance(clazz, argumentTypes, arguments, EasyHandler.class);
    }

    public static <T> T enhance(Class<T> clazz, Class<?> callbackType){
        return enhance(clazz, null, null, callbackType);
    }

    public static <T> T enhance(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments, Class<?> callbackType){
        Callback callback = (Callback) origin(callbackType);
        Enhance enhance = new Enhance();
        enhance.setClass(clazz);
        enhance.setHandler(callback);
        try {
            if (Objects.isNull(argumentTypes) && Objects.isNull(arguments)) {
                return (T) enhance.create();
            }
            return (T) enhance.create(argumentTypes, arguments);
        }catch (IllegalArgumentException e){
            throw new EnhanceException(ObjectUtil.format("{} expected type and number of construction parameters are inconsistent with the actual ones", clazz.getName()), e);

        }
    }
}
