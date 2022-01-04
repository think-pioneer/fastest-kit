package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastest.core.enhance.joinpoint.field.JoinPoint;
import xyz.thinktest.fastest.core.enhance.joinpoint.field.FieldAnnotationProcessable;

/**
 * @Date: 2021/11/3
 */
public abstract class AbstractFieldProcess<T> implements FieldAnnotationProcessable<T> {
    @Override
    public void process(JoinPoint<T> joinPoint){}
}
