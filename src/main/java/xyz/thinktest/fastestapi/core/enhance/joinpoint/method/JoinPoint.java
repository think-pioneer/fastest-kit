package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;

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
     *Represented object
     */
    <T> Target getTarget();

    /**
     * The value returned to the proxied method
     */
    <T> void setReturn(T value);

    /**
     *feature realization object
     */
    <T> T getThis();
}
