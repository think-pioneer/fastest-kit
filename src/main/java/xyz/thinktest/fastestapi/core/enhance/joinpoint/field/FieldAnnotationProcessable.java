package xyz.thinktest.fastestapi.core.enhance.joinpoint.field;

import xyz.thinktest.fastestapi.core.enhance.AnnotationProcessable;

/**
 * @Date: 2021/10/28
 */
public interface FieldAnnotationProcessable<T> extends AnnotationProcessable {

    /**
     * enhance feature process method of annotation
    */

    void process(JoinPoint<T> joinPoint);
}
