package org.fastest.core.aspect.method;

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
    Object getTarget();

    /**
     *feature realization object
     */
    Object getThis();
}
