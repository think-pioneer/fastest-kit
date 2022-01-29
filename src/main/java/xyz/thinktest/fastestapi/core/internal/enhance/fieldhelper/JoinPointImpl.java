package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Date: 2021/12/6
 */
@SuppressWarnings("unchecked")
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
    public <T> Target getTarget() {
        return (Target) this.target;
    }

    @Override
    public <T> T getThis() {
        return (T) this.self;
    }
}
