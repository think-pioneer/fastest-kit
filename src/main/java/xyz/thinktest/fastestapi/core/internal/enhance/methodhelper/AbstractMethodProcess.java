package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.JoinPoint;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.MethodAnnotationProcessable;

/**
 * @Date: 2021/11/28
 */
public abstract class AbstractMethodProcess implements MethodAnnotationProcessable {

    @Override
    public abstract void process(JoinPoint joinPoint);
}
