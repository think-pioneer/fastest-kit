package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Date: 2021/12/6
 */
@SuppressWarnings("unchecked")
class JoinPointMeta implements JoinPoint {
    private final Annotation annotation;
    private final Method method;
    private final Object[] args;
    private final Target target;
    private final Object self;
    private Object returnValue;

    public JoinPointMeta(Annotation annotation, Method method, Object[] args, Target target, Object self){
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
    public <T> T getArg(Class<T> clazz, int index){
        if(null == this.args || this.args.length == 0){
            return null;
        }
        int flag = 0;
        index = Math.max(index, 1);
        for(Object arg:this.args){
            if(arg.getClass().equals(clazz)){
                if(flag == index - 1) {
                    return (T) arg;
                }
                flag ++;
            }
        }
        return null;
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
