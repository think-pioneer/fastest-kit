package xyz.thinktest.fastest.utils.reflects;

import xyz.thinktest.fastest.common.exceptions.FastestBasicException;
import xyz.thinktest.fastest.common.exceptions.ReflectionException;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Objects;

/**
 * @Date: 2021/12/26
 */
@SuppressWarnings("unchecked")
public class ReflectUtil {

    public static boolean isPublic(Member member){
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isPrivate(Member member){
        return Modifier.isPrivate(member.getModifiers());
    }

    public static boolean isProtected(Member member){
        return Modifier.isProtected(member.getModifiers());
    }

    public static boolean isStatic(Member member){
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isFinal(Member member){
        return Modifier.isFinal(member.getModifiers());
    }

    public static boolean isSynchronized(Member member){
        return Modifier.isSynchronized(member.getModifiers());
    }

    public static boolean isVolatile(Member member){
        return Modifier.isVolatile(member.getModifiers());
    }

    public static boolean isTransient(Member member){
        return Modifier.isTransient(member.getModifiers());
    }

    public static boolean isNative(Member member){
        return Modifier.isNative(member.getModifiers());
    }

    public static boolean isInterface(Member member){
        return Modifier.isInterface(member.getModifiers());
    }

    public static boolean isAbstract(Member member){
        return Modifier.isAbstract(member.getModifiers());
    }

    public static boolean isStrict(Member member){
        return Modifier.isStrict(member.getModifiers());
    }

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
