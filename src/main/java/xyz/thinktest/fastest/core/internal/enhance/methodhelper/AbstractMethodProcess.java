package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.core.cnhance.method.JoinPoint;
import xyz.thinktest.fastest.core.cnhance.method.MethodAnnotationProcessable;

/**
 * @Date: 2021/11/28
 */
public abstract class AbstractMethodProcess implements MethodAnnotationProcessable {

    @Override
    public abstract void process(JoinPoint joinPoint);
}