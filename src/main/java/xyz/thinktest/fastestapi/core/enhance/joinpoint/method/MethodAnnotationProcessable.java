package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.enhance.AnnotationProcessable;

/**
 * @Date: 2021/11/28
 * annotation process interface
 */
public interface MethodAnnotationProcessable extends AnnotationProcessable {

    void process(JoinPoint joinPoint);
}
