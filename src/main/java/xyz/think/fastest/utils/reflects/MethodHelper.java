package xyz.think.fastest.utils.reflects;

import xyz.think.fastest.common.exceptions.ReflectionException;
import xyz.think.fastest.utils.string.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @Date: 2021/12/26
 */
@SuppressWarnings("unchecked")
public class MethodHelper {
    private final Object instance;
    private final Class<?> instanceType;
    private final String methodName;
    private final Method method;

    private MethodHelper(Object instance, String methodName, Class<?>[] argTypes){
        if(Objects.isNull(instance)){
            throw new ReflectionException("The instance where the method is located cannot be null");
        }
        if(StringUtils.isEmpty(methodName)){
            throw new ReflectionException("Method name cannot be null or empty");
        }
        if(Objects.isNull(argTypes)){
            throw new ReflectionException("method params type could not null");
        }
        this.instance = instance instanceof Class ? null : instance;
        this.methodName = methodName;
        this.instanceType = instance instanceof Class ? (Class<?>) instance : instance.getClass();
        if (argTypes.length == 0) {
            this.method = ReflectUtil.getDeclaredMethod(this.instanceType, methodName);
        } else {
            this.method = ReflectUtil.getDeclaredMethod(this.instanceType, methodName, argTypes);
        }
        this.method.setAccessible(true);
    }

    private MethodHelper(Object instance, Method method){
        if(Objects.isNull(instance) || Objects.isNull(method)){
            throw new ReflectionException("The instance where the method is located cannot be null");
        }
        this.instance = instance instanceof Class ? null : instance;;
        this.methodName = method.getName();
        this.instanceType = instance instanceof Class ? (Class<?>) instance : instance.getClass();
        this.method = method;
        this.method.setAccessible(true);
    }

    public <T> T invoke(Object... args){
        try {
            if (ReflectUtil.isStatic(method)) {
                return (T) method.invoke(null, args);
            } else {
                return (T) method.invoke(instance, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(StringUtils.format("exception executing method:{0} in class{1}", this.methodName, instanceType.getName()));
        }
    }

    public <T> boolean hasParameter(T expect){
        Type[] parameterTypes = this.method.getParameterTypes();
        for(Type type:parameterTypes){
            if (type.getTypeName().equals(expect.getClass().getName())){
                return true;
            }
        }
        return false;
    }

    public static MethodHelper getInstance(Object instance, String methodName, Class<?>... argTypes){
        return new MethodHelper(instance, methodName, argTypes);
    }

    public static MethodHelper getInstance(Object instance, Method method){
        return new MethodHelper(instance, method);
    }
}
