package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.core.aspect.method.JoinPoint;
import org.fastest.core.internal.enhance.AnnotationProcessable;

/**
 * @Date: 2021/11/28
 * annotation process interface
 */
public interface MethodAnnotationProcessable extends AnnotationProcessable {

    void process(JoinPoint joinPoint);
}
