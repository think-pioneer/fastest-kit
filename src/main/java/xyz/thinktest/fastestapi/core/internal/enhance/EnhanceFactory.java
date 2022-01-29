package xyz.thinktest.fastestapi.core.internal.enhance;

import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.utils.ObjectUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @Date: 2020/10/29
 */
@SuppressWarnings("unchecked")
public final class EnhanceFactory {
    private EnhanceFactory(){}

    public static <T> T origin(Class<T> clazz){
        return origin(clazz, null, null);
    }

    public static <T> T origin(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        if(clazz.isInterface()){
            throw new FastestBasicException(ObjectUtil.format("{} is interface, could not instantiation", clazz));
        }
        try{
            if(Objects.isNull(argumentTypes) && Objects.isNull(arguments)){
                return clazz.getDeclaredConstructor().newInstance();
            }
            return clazz.getDeclaredConstructor(argumentTypes).newInstance(arguments);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new FastestBasicException(ObjectUtil.format("create {} instance error", clazz), e);
        }
    }

    public static <T> T enhance(Class<T> clazz){
        return enhance(clazz, null, null);
    }

    public static <T> T enhance(Class<T> clazz, Class<?>[] argumentTypes, Object[] arguments){
        Enhance enhance = new Enhance();
        enhance.setClass(clazz);
        enhance.setHandler(new MethodHandler());
        try {
            if (Objects.isNull(argumentTypes) && Objects.isNull(arguments)) {
                return (T) enhance.create();
            }
            return (T) enhance.create(argumentTypes, arguments);
        }catch (IllegalArgumentException e){
            throw new EnhanceException(ObjectUtil.format("{} expected type and number of construction parameters are inconsistent with the actual ones", clazz), e);

        }
    }
}
