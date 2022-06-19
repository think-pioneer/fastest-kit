package xyz.thinktest.fastestapi.utils.reflects;

import xyz.thinktest.fastestapi.common.exceptions.ReflectionException;
import xyz.thinktest.fastestapi.utils.string.StringUtils;

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

    public static <T extends Member> boolean isSynthetic(T clazz){
        return null != clazz && clazz.isSynthetic();
    }

    public static boolean isSubclasses(Class<?> src, Class<?> dst){
        return null != src && null != dst && src.isAssignableFrom(dst);
    }

    public static boolean isArray(Class<?> clazz){
        return null != clazz && clazz.isArray();
    }

    public static boolean isAnnotation(Class<?> clazz){
        return null != clazz && clazz.isAnnotation();
    }

    public static boolean isPrimitive(Class<?> clazz){
        return null != clazz && clazz.isPrimitive();
    }

    public static boolean isInstance(Class<?> clazz, Object object){
        return null != clazz && clazz.isInstance(object);
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
    public static <T> T getValueByProxy(Object object, String name){
        try{
            InvocationHandler handler = Proxy.getInvocationHandler(object);
            Field fieldHandler = handler.getClass().getDeclaredField("memberValues");
            fieldHandler.setAccessible(true);
            Map<?, ?> o = (Map<?, ?>) fieldHandler.get(handler);
            return (T) o.get(name);
        } catch (Exception e){
            throw new ReflectionException(StringUtils.format("get filed value error:{0}", e.getMessage()), e.getCause());
        }
    }

    public static <T> T getValue(Field field, Object obj){
        try{
            return (T) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T getValue(String fieldName, Object obj){
        return getValue(getDeclaredField(obj, fieldName), obj);
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
            throw new ReflectionException("get Collection element real type error: type is null or ont inherit ParameterizedType");
        }catch (Exception e){
            throw new ReflectionException(StringUtils.format("get Collection element real type error:{0}", e.getMessage()), e.getCause());
        }
    }


    public static Class<?> getCollectionGenericRealType(Object obj, String collectionName){
        return getCollectionGenericRealType(getDeclaredField(obj, collectionName).getGenericType());
    }

    /**
     * 获取接口上的泛型T
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceT(Object o, int index) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);
    }


    /**
     * 获取类上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        return checkType(type, index);
    }

    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    public static Field getField(Class<?> clazz, String name){
        try {
            return clazz.getField(name);
        }catch (NoSuchFieldException e){
            throw new ReflectionException("no such field from " + clazz.getCanonicalName());
        }
    }

    public static Field getField(Object obj, String name){
        if(obj instanceof Class){
            return getField((Class<?>) obj, name);
        }
        return getField(obj.getClass(), name);
    }

    public static Field getDeclaredField(Class<?> clazz, String name){
        try {
            return clazz.getDeclaredField(name);
        }catch (NoSuchFieldException e){
            throw new ReflectionException("no such field from " + clazz.getCanonicalName());
        }
    }

    public static Field getDeclaredField(Object obj, String name){
        if(obj instanceof Class){
            return getDeclaredField((Class<?>) obj, name);
        }
        return getDeclaredField(obj.getClass(), name);
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... paramsTypes){
        try {
            return clazz.getMethod(name, paramsTypes);
        }catch (NoSuchMethodException e){
            throw new ReflectionException("no such method from " + clazz.getCanonicalName());
        }
    }

    public static Method getMethod(Object obj, String name, Class<?>... paramsTypes){
        if(obj instanceof Class){
            return getMethod((Class<?>) obj, name, paramsTypes);
        }
        return getMethod(obj.getClass(), name, paramsTypes);
    }

    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... paramsTypes){
        try {
            return clazz.getDeclaredMethod(name, paramsTypes);
        }catch (NoSuchMethodException e){
            throw new ReflectionException("no such method from " + clazz.getCanonicalName());
        }
    }

    public static Method getDeclaredMethod(Object obj, String name, Class<?>... paramsTypes){
        if(obj instanceof Class){
            return getDeclaredMethod((Class<?>) obj, name, paramsTypes);
        }
        return getDeclaredMethod(obj.getClass(), name, paramsTypes);
    }

    public static <T extends Annotation> T getClassAnnotation(Class<?> clazz, Class<T> annotation){
        if(clazz == null || annotation == null){
            throw new ReflectionException("class and annotation not could null");
        }
        try {
            return clazz.getAnnotation(annotation);
        }catch (Throwable e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }

    public static <T extends Annotation> T getClassAnnotation(Object obj, Class<T> annotation){
        if(obj instanceof Class){
            return getClassAnnotation((Class<?>) obj, annotation);
        }
        return getClassAnnotation(obj.getClass(), annotation);
    }

    public static <T extends Annotation> T getDeclaredClassAnnotation(Class<?> clazz, Class<T> annotation){
        if(clazz == null || annotation == null){
            throw new ReflectionException("class and annotation not could null");
        }
        try {
            return clazz.getDeclaredAnnotation(annotation);
        }catch (Throwable e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }

    public static <T extends Annotation> T getDeclaredClassAnnotation(Object obj, Class<T> annotation) {
        if(obj instanceof Class){
            return getDeclaredClassAnnotation((Class<?>) obj, annotation);
        }
        return getDeclaredClassAnnotation(obj.getClass(), annotation);
    }

    public static <T extends Annotation> T getMethodAnnotation(Class<?> clazz, String name, Class<?>[] paramsTypes, Class<T> annotation){
        try{
            return getMethod(clazz, name, paramsTypes).getAnnotation(annotation);
        }catch (Exception e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }
    public static <T extends Annotation> T getMethodAnnotation(Object obj, String name, Class<?>[] paramsTypes, Class<T> annotation) {
        if (obj instanceof Class){
            return getMethodAnnotation((Class<?>) obj, name, paramsTypes, annotation);
        }
        return getMethodAnnotation(obj.getClass(), name, paramsTypes, annotation);
    }

    public static <T extends Annotation> T getDeclaredMethodAnnotation(Class<?> clazz, String name, Class<?>[] paramsTypes, Class<T> annotation){
        try{
            return getDeclaredMethod(clazz, name, paramsTypes).getAnnotation(annotation);
        }catch (Exception e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }
    public static <T extends Annotation> T getDeclaredMethodAnnotation(Object obj, String name, Class<?>[] paramsTypes, Class<T> annotation) {
        if (obj instanceof Class){
            return getDeclaredMethodAnnotation((Class<?>) obj, name, paramsTypes, annotation);
        }
        return getDeclaredMethodAnnotation(obj.getClass(), name, paramsTypes, annotation);
    }

    public Annotation[] getMethodAnnotations(Class<?> clazz, String name, Class<?>... paramsTypes){
        try{
            return getMethod(clazz, name, paramsTypes).getAnnotations();
        }catch (Exception e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }

    public Annotation[] getMethodAnnotations(Object obj, String name, Class<?>... paramsTypes){
        if(obj instanceof Class){
            return getMethodAnnotations((Class<?>) obj, name, paramsTypes);
        }
        return getMethodAnnotations(obj.getClass(), name, paramsTypes);
    }

    public Annotation[] getDeclaredMethodAnnotations(Class<?> clazz, String methodName, Class<?>... paramsTypes){
        try{
            return getDeclaredMethod(clazz, methodName, paramsTypes).getDeclaredAnnotations();
        }catch (Exception e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }

    public Annotation[] getDeclaredMethodAnnotations(Object obj, String methodName, Class<?>... paramsTypes){
        if(obj instanceof Class){
            return getDeclaredMethodAnnotations((Class<?>) obj, methodName, paramsTypes);
        }
        return getDeclaredMethodAnnotations(obj.getClass(), methodName, paramsTypes);
    }

    public Annotation[] getClassAnnotations(Class<?> clazz){
        try{
            return clazz.getAnnotations();
        }catch (Exception e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }

    public Annotation[] getClassAnnotations(Object obj){
        if(obj instanceof Class){
            return getClassAnnotations((Class<?>) obj);
        }
        return getClassAnnotations(obj.getClass());
    }

    public Annotation[] getDeclaredClassAnnotations(Class<?> clazz){
        try{
            return clazz.getDeclaredAnnotations();
        }catch (Exception e){
            throw new ReflectionException(StringUtils.format("reflect {0} exception: ", clazz.getCanonicalName()), e);
        }
    }

    public Annotation[] getDeclaredClassAnnotations(Object obj){
        if(obj instanceof Class){
            return getDeclaredClassAnnotations((Class<?>) obj);
        }
        return getDeclaredClassAnnotations(obj.getClass());
    }
}
