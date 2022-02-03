package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Date: 2021/12/6
 */
@SuppressWarnings("unchecked")
class JoinPointImpl implements JoinPoint {
    private final Annotation annotation;
    private final Method method;
    private final Object[] args;
    private final Target target;
    private final Object self;
    private Object returnValue;

    public JoinPointImpl(Annotation annotation, Method method, Object[] args, Target target, Object self){
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
    public Target getTarget() {
        return this.target;
    }

    @Override
    public <T> void setReturn(T value) {
        this.returnValue = value;
    }

    public <T> T getReturnValue(){
        return (T) returnValue;
    }

    @Override
    public <T> T getThis() {
        return (T) this.self;
    }
}
