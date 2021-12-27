package org.fastest.core.internal.enhance.fieldhelper;

import org.fastest.core.cnhance.field.JoinPoint;
import org.fastest.core.cnhance.field.FieldAnnotationProcessable;

/**
 * @Date: 2021/11/3
 */
public abstract class AbstractFieldProcess implements FieldAnnotationProcessable {
    @Override
    public void process(JoinPoint joinPoint){}
}
