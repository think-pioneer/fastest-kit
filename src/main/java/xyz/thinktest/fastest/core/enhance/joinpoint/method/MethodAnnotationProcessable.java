package xyz.thinktest.fastest.core.enhance.joinpoint.method;

import xyz.thinktest.fastest.core.enhance.AnnotationProcessable;

/**
 * @Date: 2021/11/28
 * annotation process interface
 */
public interface MethodAnnotationProcessable<T> extends AnnotationProcessable {

    void process(JoinPoint<T> joinPoint);
}
