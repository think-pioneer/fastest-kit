package xyz.thinktest.fastestapi.utils.reflects;

import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.ReflectionException;

import java.lang.annotation.Annotation;
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

    public static <T extends Member> boolean isSynthetic(T class_){
        return null != class_ && class_.isSynthetic();
    }

    public static boolean isSubclasses(Class<?> src, Class<?> dst){
        return null != src && null != dst && src.isAssignableFrom(dst);
    }

    public static boolean isArray(Class<?> class_){
        return null != class_ && class_.isArray();
    }

    public static boolean isAnnotation(Class<?> class_){
        return null != class_ && class_.isAnnotation();
    }

    public static boolean isPrimitive(Class<?> class_){
        return null != class_ && class_.isPrimitive();
    }

    public static boolean isInstance(Class<?> class_, Object object){
        return null != class_ && class_.isInstance(object);
    }

    public static boolean isBridge(Method method){
        return null != method && method.isBridge();
    }

    public static boolean isVarArgs(Method method){
        return null != method && method.isVarArgs();
    }

    public static boolean isDefault(Method method){
        return null != method && method.isDefault();
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

    public static <T> Class<T> forName(String name){
        try{
            return (Class<T>) Class.forName(name);
        }catch (Exception e){
            throw new ReflectionException("not found class:" + name, e.getCause());
        }
    }

    public static <T> Class<T> forName(String name, boolean initialize, ClassLoader loader){
        try{
            return (Class<T>) Class.forName(name, initialize, loader);
        }catch (Exception e){
            throw new ReflectionException("not found class:" + name, e.getCause());
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
            throw new ReflectionException(ObjectUtil.format("get Collection element real type error: type is null or ont inherit ParameterizedType"));
        }catch (Exception e){
            throw new ReflectionException(ObjectUtil.format("get Collection element real type error:{}", e.getMessage()), e.getCause());
        }
    }

    public static Field getField(Class<?> class_, String name){
        try {
            return class_.getField(name);
        }catch (NoSuchFieldException e){
            throw new ReflectionException("no such field from " + class_.getCanonicalName());
        }
    }

    public static Field getDeclaredField(Class<?> class_, String name){
        try {
            return class_.getDeclaredField(name);
        }catch (NoSuchFieldException e){
            throw new ReflectionException("no such field from " + class_.getCanonicalName());
        }
    }

    public static Method getMethod(Class<?> class_, String name){
        try {
            return class_.getMethod(name);
        }catch (NoSuchMethodException e){
            throw new ReflectionException("no such method from " + class_.getCanonicalName());
        }
    }

    public static Method getDeclaredMethod(Class<?> class_, String name){
        try {
            return class_.getDeclaredMethod(name);
        }catch (NoSuchMethodException e){
            throw new ReflectionException("no such method from " + class_.getCanonicalName());
        }
    }

    public static <T extends Annotation> T getMethod(Class<?> class_, Class<T> annotation){
        if(class_ == null || annotation == null){
            throw new ReflectionException("class and annotation not could null");
        }
        try {
            return class_.getAnnotation(annotation);
        }catch (Throwable e){
            throw new ReflectionException(ObjectUtil.format("reflect {} exception: ", class_.getCanonicalName()), e);
        }
    }

    public static <T extends Annotation> T getDeclaredMethod(Class<?> class_, Class<T> annotation){
        if(class_ == null || annotation == null){
            throw new ReflectionException("class and annotation not could null");
        }
        try {
            return class_.getDeclaredAnnotation(annotation);
        }catch (Throwable e){
            throw new ReflectionException(ObjectUtil.format("reflect {} exception: ", class_.getCanonicalName()), e);
        }
    }
}
