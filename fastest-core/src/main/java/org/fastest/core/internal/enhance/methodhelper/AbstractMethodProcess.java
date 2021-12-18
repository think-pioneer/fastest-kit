package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.core.aspect.method.JoinPoint;

/**
 * @Date: 2021/11/28
 */
public abstract class AbstractMethodProcess implements MethodAnnotationProcessable {

    @Override
    public void process(JoinPoint joinPoint){}
}
