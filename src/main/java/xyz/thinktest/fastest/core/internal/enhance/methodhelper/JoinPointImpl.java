package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.core.cnhance.method.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Date: 2021/12/6
 */
class JoinPointImpl implements JoinPoint {
    private final Annotation annotation;
    private final Method method;
    private final Object[] args;
    private final Object target;
    private final Object self;
    private Object returnValue;

    public JoinPointImpl(Annotation annotation, Method method, Object[] args, Object target, Object self){
        this.annotation = annotation;
        this.method = method;
        this.args = args;
        this.target = target;
        this.self = self;
    }

    @Override
    public Annotation getAnnotation() {
        return this.annotation;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArgs() {
        return this.args;
    }

    @Override
    public Object getTarget() {
        return this.target;
    }

    @Override
    public void setReturn(Object value) {
        this.returnValue = value;
    }

    public Object getReturnValue(){
        return returnValue;
    }

    @Override
    public Object getThis() {
        return this.self;
    }
}
