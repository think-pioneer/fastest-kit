package xyz.thinktest.fastest.core.cnhance.method;

import xyz.thinktest.fastest.core.cnhance.AnnotationProcessable;

/**
 * @Date: 2021/11/28
 * annotation process interface
 */
public interface MethodAnnotationProcessable extends AnnotationProcessable {

    void process(JoinPoint joinPoint);
}
