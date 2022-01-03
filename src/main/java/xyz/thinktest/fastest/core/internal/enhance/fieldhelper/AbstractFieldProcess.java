package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastest.core.enhance.field.JoinPoint;
import xyz.thinktest.fastest.core.enhance.field.FieldAnnotationProcessable;

/**
 * @Date: 2021/11/3
 */
public abstract class AbstractFieldProcess implements FieldAnnotationProcessable {
    @Override
    public void process(JoinPoint joinPoint){}
}
