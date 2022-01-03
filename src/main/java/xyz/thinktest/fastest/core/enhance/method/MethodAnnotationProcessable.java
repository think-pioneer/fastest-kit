package xyz.thinktest.fastest.core.enhance.method;

import xyz.thinktest.fastest.core.enhance.AnnotationProcessable;

/**
 * @Date: 2021/11/28
 * annotation process interface
 */
public interface MethodAnnotationProcessable extends AnnotationProcessable {

    void process(JoinPoint joinPoint);
}
