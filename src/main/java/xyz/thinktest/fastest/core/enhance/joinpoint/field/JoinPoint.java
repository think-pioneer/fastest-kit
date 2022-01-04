package xyz.thinktest.fastest.core.enhance.joinpoint.field;

import xyz.thinktest.fastest.core.enhance.joinpoint.Target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Date: 2021/12/6
 */
public interface JoinPoint<T> {

    /**
     *annotation object
     */
    Annotation getAnnotation();

    /**
     * Intercepted field
     */
    Field getField();

    /**
     *Intercepted field object
     */
    Target<T> getTarget();

    /**
     * feature realization object
     */

    Object getThis();
}
