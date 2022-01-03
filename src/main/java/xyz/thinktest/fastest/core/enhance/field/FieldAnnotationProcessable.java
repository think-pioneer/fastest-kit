package xyz.thinktest.fastest.core.enhance.field;

import xyz.thinktest.fastest.core.enhance.AnnotationProcessable;

/**
 * @Date: 2021/10/28
 */
public interface FieldAnnotationProcessable extends AnnotationProcessable {

    /**
     * enhance feature process method of annotation
    */

    void process(JoinPoint joinPoint);
}
