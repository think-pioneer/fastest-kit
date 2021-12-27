package org.fastest.utils.reflects;

import org.apache.commons.lang3.StringUtils;
import org.fastest.common.exceptions.EnhanceException;
import org.fastest.common.exceptions.ReflectionException;
import org.fastest.utils.ObjectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @Date: 2021/12/26
 */
@SuppressWarnings("unchecked")
public class MethodHelper<T> {
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
        this.instance = instance;
        this.methodName = methodName;
        this.instanceType = instance.getClass();
        try {
            if (argTypes.length == 0) {
                this.method = this.instanceType.getMethod(methodName);
            } else {
                this.method = this.instanceType.getMethod(methodName, argTypes);
            }
            this.method.setAccessible(true);
        }catch (NoSuchMethodException e) {
            throw new EnhanceException(ObjectUtil.format("not found method:{} in class:{}", methodName, this.instanceType.getName()), e);
        }
    }

    private MethodHelper(Object instance, Method method){
        if(Objects.isNull(instance) || Objects.isNull(method)){
            throw new ReflectionException("The instance where the method is located cannot be null");
        }
        this.instance = instance;
        this.methodName = method.getName();
        this.instanceType = instance.getClass();
        this.method = method;
        this.method.setAccessible(true);
    }

    public T invoke(Object[] args){
        try {
            if (ReflectUtil.isStatic(method)) {
                return (T) method.invoke(null, args);
            } else {
                return (T) method.invoke(instance, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e){
            throw new EnhanceException(ObjectUtil.format("exception executing method:{} in class{}", this.methodName, instanceType.getName()));
        }
    }

    public boolean hasParameter(T expect){
        Type[] parameterTypes = this.method.getParameterTypes();
        for(Type type:parameterTypes){
            if (type.getTypeName().equals(expect.getClass().getName())){
                return true;
            }
        }
        return false;
    }

    public static <T> MethodHelper<T> getInstance(Object instance, String methodName, Class<?>[] argTypes){
        return new MethodHelper<>(instance, methodName, argTypes);
    }

    public static <T> MethodHelper<T> getInstance(Object instance, Method method){
        return new MethodHelper<>(instance, method);
    }
}
