package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.core.enhance.joinpoint.Target;
import xyz.thinktest.fastest.core.enhance.joinpoint.method.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Date: 2021/12/6
 */
class JoinPointImpl<T> implements JoinPoint<T> {
    private final Annotation annotation;
    private final Method method;
    private final Object[] args;
    private final Target<T> target;
    private final T self;
    private T returnValue;

    public JoinPointImpl(Annotation annotation, Method method, Object[] args, Target<T> target, T self){
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
    public Target<T> getTarget() {
        return this.target;
    }

    @Override
    public void setReturn(T value) {
        this.returnValue = value;
    }

    public T getReturnValue(){
        return returnValue;
    }

    @Override
    public T getThis() {
        return this.self;
    }
}
