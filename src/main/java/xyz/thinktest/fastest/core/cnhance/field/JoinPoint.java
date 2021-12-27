package xyz.thinktest.fastest.core.cnhance.field;

import xyz.thinktest.fastest.core.internal.enhance.fieldhelper.Target;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Date: 2021/12/6
 */
public interface JoinPoint {

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
    Target getTarget();

    /**
     * feature realization object
     */

    Object getThis();
}
