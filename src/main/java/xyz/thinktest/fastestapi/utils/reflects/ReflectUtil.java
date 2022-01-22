package xyz.thinktest.fastestapi.utils.reflects;

import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.common.exceptions.ReflectionException;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Objects;

/**
 * @Date: 2021/12/26
 */
@SuppressWarnings("unchecked")
public class ReflectUtil {

    public static <T> boolean isPublic(T object){
        return Modifier.isPublic(getModifiers(object));
    }

    public static <T> boolean isPrivate(T object){
        return Modifier.isPrivate(getModifiers(object));
    }

    public static <T> boolean isProtected(T object){
        return Modifier.isProtected(getModifiers(object));
    }

    public static <T> boolean isStatic(T object){
        return Modifier.isStatic(getModifiers(object));
    }

    public static <T> boolean isFinal(T object){
        return Modifier.isFinal(getModifiers(object));
    }

    public static <T> boolean isSynchronized(T object){
        return Modifier.isSynchronized(getModifiers(object));
    }

    public static <T> boolean isVolatile(T object){
        return Modifier.isVolatile(getModifiers(object));
    }

    public static <T> boolean isTransient(T object){
        return Modifier.isTransient(getModifiers(object));
    }

    public static <T> boolean isNative(T object){
        return Modifier.isNative(getModifiers(object));
    }

    public static <T> boolean isInterface(T object){
        return Modifier.isInterface(getModifiers(object));
    }

    public static <T> boolean isAbstract(T object){
        return Modifier.isAbstract(getModifiers(object));
    }

    public static <T> boolean isStrict(T object){
        return Modifier.isStrict(getModifiers(object));
    }

    /**
     * 获得字段、方法、构造方法、类等对象的修饰符
     */
    public static <T> int getModifiers(T object){
        if(object instanceof Member){
            Member member = (Member) object;
            return member.getModifiers();
        } else if(object instanceof Class){
            Class<?> _class = (Class<?>) object;
            return _class.getModifiers();
        }
        return -1;
    }

    /**
     * 获得对象的值
     */
    public static <T> T get(Object object, String name){
        try{
            InvocationHandler handler = Proxy.getInvocationHandler(object);
            Field fieldHandler = handler.getClass().getDeclaredField("memberValues");
            fieldHandler.setAccessible(true);
            Map<?, ?> o = (Map<?, ?>) fieldHandler.get(handler);
            return (T) o.get(name);
        } catch (Exception e){
            throw new ReflectionException(ObjectUtil.format("get filed value error:{}", e.getMessage()), e.getCause());
        }
    }

    /**
     * Gets the true type of the collection element
     */
    public static Class<?> getCollectionGenericRealType(Type type){
        try {
            if(Objects.nonNull(type) && type instanceof ParameterizedType){
                ParameterizedType pt = (ParameterizedType) type;
                // 得到泛型里的class类型对象
                return (Class<?>)pt.getActualTypeArguments()[0];
            }
            throw new FastestBasicException(ObjectUtil.format("get Collection element real type error: type is null or ont inherit ParameterizedType"));
        }catch (Exception e){
            throw new FastestBasicException(ObjectUtil.format("get Collection element real type error:{}", e.getMessage()), e.getCause());
        }
    }
}
