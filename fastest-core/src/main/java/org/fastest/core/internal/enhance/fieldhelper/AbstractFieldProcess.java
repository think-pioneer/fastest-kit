package org.fastest.core.internal.enhance.fieldhelper;

import org.fastest.core.aspect.field.JoinPoint;

/**
 * @Date: 2021/11/3
 */
public abstract class AbstractFieldProcess implements FieldAnnotationProcessable {
    @Override
    public void process(JoinPoint joinPoint){}
}
