package org.fastest.core.internal.enhance.fieldhelper;

import org.fastest.core.aspect.field.JoinPoint;
import org.fastest.core.internal.enhance.AnnotationProcessable;

/**
 * @Date: 2021/10/28
 */
public interface FieldAnnotationProcessable extends AnnotationProcessable {

    /**
     * enhance feature process method of annotation
    */

    void process(JoinPoint joinPoint);
}
