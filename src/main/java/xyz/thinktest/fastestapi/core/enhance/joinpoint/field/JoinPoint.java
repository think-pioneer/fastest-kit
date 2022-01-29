package xyz.thinktest.fastestapi.core.enhance.joinpoint.field;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;

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
    <T> Target getTarget();

    /**
     * feature realization object
     */

    <T> T getThis();
}
