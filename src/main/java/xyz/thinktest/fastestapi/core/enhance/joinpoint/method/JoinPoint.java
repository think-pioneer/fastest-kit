package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Date: 2021/12/6
 */
public interface JoinPoint<T> {

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
     *Represented object
     */
    Target<T> getTarget();

    /**
     * The value returned to the proxied method
     */
    void setReturn(T value);

    /**
     *feature realization object
     */
    T getThis();
}