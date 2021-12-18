package org.fastest.core.internal.enhance;

import net.sf.cglib.proxy.Callback;
import org.fastest.common.exceptions.EnhanceException;
import org.fastest.common.exceptions.FastestBasicException;
import org.fastest.utils.ObjectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @Date: 2020/10/29
 */
public class EnhanceFactory {
    private EnhanceFactory(){}

    public static Object origin(Class<?> clazz){
        try{
            return clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new FastestBasicException(ObjectUtil.format("create {} instance error", clazz.getName()));
        }
    }

    public static Object origin(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments){
        try{
            Constructor<?> constructor = clazz.getConstructor(argumentTypes);
            return constructor.newInstance(arguments);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new FastestBasicException(ObjectUtil.format("create {} instance error", clazz.getName()), e);
        }
    }

    public static Object enhance(Class<?> clazz, Class<?> callbackType){
        return enhance(clazz, null, null, callbackType);
    }

    public static Object enhance(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments, Class<?> callbackType){
        Callback callback = (Callback) origin(callbackType);
        Enhance enhance = new Enhance();
        enhance.setClass(clazz);
        enhance.setHandler(callback);
        try {
            if (Objects.isNull(argumentTypes) && Objects.isNull(arguments)) {
                return enhance.create();
            }
            return enhance.create(argumentTypes, arguments);
        }catch (IllegalArgumentException e){
            throw new EnhanceException(ObjectUtil.format("{} expected type and number of construction parameters are inconsistent with the actual ones", clazz.getName()), e);

        }
    }
}
