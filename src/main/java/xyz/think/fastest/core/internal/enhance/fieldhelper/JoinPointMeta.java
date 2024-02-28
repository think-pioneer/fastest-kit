package xyz.think.fastest.core.internal.enhance.fieldhelper;

import xyz.think.fastest.core.enhance.joinpoint.Target;
import xyz.think.fastest.core.enhance.joinpoint.field.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 切面元数据
 * @Date: 2021/12/6
 */
@SuppressWarnings("unchecked")
class JoinPointMeta implements JoinPoint {
    private final Annotation annotation;
    private final Field field;
    private final Target target;
    private final Object self;

    public JoinPointMeta(Annotation annotation, Field field, Target target, Object self){
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
    public <T> T getThis() {
        return (T) this.self;
    }
}
