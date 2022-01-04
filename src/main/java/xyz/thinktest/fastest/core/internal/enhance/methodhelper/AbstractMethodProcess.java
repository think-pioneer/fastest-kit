package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.core.enhance.joinpoint.method.JoinPoint;
import xyz.thinktest.fastest.core.enhance.joinpoint.method.MethodAnnotationProcessable;

/**
 * @Date: 2021/11/28
 */
public abstract class AbstractMethodProcess<T> implements MethodAnnotationProcessable<T> {

    @Override
    public abstract void process(JoinPoint<T> joinPoint);
}
