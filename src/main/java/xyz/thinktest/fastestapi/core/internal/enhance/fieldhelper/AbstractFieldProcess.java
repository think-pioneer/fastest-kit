package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.FieldAnnotationProcessable;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;

/**
 * @Date: 2021/11/3
 */
public abstract class AbstractFieldProcess implements FieldAnnotationProcessable {
    @Override
    public void process(JoinPoint joinPoint){}
}
