package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.core.cnhance.method.JoinPoint;
import org.fastest.core.cnhance.method.MethodAnnotationProcessable;

/**
 * @Date: 2021/11/28
 */
public abstract class AbstractMethodProcess implements MethodAnnotationProcessable {

    @Override
    public abstract void process(JoinPoint joinPoint);
}
