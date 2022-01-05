package xyz.thinktest.fastest.core.internal.enhance;

import xyz.thinktest.fastest.common.exceptions.EnhanceException;
import xyz.thinktest.fastest.common.exceptions.FastestBasicException;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @Date: 2020/10/29
 */
@SuppressWarnings("unchecked")
public final class EnhanceFactory {
    private EnhanceFactory(){}

    public static <T> T origin(Class<T> clazz){
        try{
            return clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new FastestBasicException(ObjectUtil.format("create {} instance error", clazz.getName()));
        }
    }

    public static <T> T origin(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        try{
            return clazz.getDeclaredConstructor(argumentTypes).newInstance(arguments);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new FastestBasicException(ObjectUtil.format("create {} instance error", clazz.getName()));
        }
    }

    public static <T> T enhance(Class<T> clazz){
        return enhance(clazz, null, null);
    }

    public static <T> T enhance(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        Enhance enhance = new Enhance();
        enhance.setClass(clazz);
        enhance.setHandler(new MethodHandler<T>());
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
