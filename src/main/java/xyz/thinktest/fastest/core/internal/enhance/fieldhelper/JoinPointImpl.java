package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastest.core.enhance.field.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Date: 2021/12/6
 */
class JoinPointImpl implements JoinPoint {
    private final Annotation annotation;
    private final Field field;
    private final Target target;
    private final Object self;

    public JoinPointImpl(Annotation annotation, Field field, Target target, Object self){
        this.annotation = annotation;
        this.field = field;
        this.target = target;
        this.self = self;
    }

    @Override
    public Annotation getAnnotation() {
        return this.annotation;
    }

    @Override
    public Field getField() {
        return this.field;
    }

    @Override
    public Target getTarget() {
        return this.target;
    }

    @Override
    public Object getThis() {
        return this.self;
    }
}
