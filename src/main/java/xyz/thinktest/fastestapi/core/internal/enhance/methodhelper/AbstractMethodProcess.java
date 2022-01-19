package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.JoinPoint;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.MethodAnnotationProcessable;

/**
 * @Date: 2021/11/28
 */
public abstract class AbstractMethodProcess<T> implements MethodAnnotationProcessable<T> {

    @Override
    public abstract void process(JoinPoint<T> joinPoint);
}
