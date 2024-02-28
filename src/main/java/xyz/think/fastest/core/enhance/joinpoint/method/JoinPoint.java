package xyz.think.fastest.core.enhance.joinpoint.method;

import xyz.think.fastest.core.enhance.joinpoint.Target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Date: 2021/12/6
 */
public interface JoinPoint {

    /**
     * annotation object
     */
    Annotation getAnnotation();

    /**
     * Intercepted method
     */
    Method getMethod();

    /**
     * Intercepted method args
     */
    Object[] getArgs();

    /**
     *from all args choose one arg
     * @param clazz arg class type
     * @param index If you have more than one parameter of the same type, select the first few parameters
     *              start 1(default)
     */
    <T> T getArg(Class<T> clazz, int index);

    /**
     *Represented object
     */
    Target getTarget();

    /**
     * The value returned to the proxied method
     */
    <T> void setReturn(T value);

    /**
     *feature realization object
     */
    <T> T getThis();

    default <T> T getArg(Class<T> clazz){
        return getArg(clazz, 1);
    }
}
