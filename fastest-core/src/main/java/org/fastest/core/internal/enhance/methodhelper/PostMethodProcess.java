package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.common.exceptions.ReflectionException;
import org.fastest.core.annotations.PostMethod;
import org.fastest.core.aspect.method.JoinPoint;
import org.fastest.utils.ObjectUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * @Date: 2021/10/29
 */
public class PostMethodProcess extends AbstractMethodProcess {

    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        PostMethod postMethod = (PostMethod) joinPoint.getAnnotation();
        Class<?> targetClass = postMethod.targetClass();
        String methodName = postMethod.method();
        int[] argsIndex = postMethod.argsIndex();
        try {
            HockMethodTool.invoke(targetClass, methodName, joinPoint.getArgs(), argsIndex);
        }catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionException(ObjectUtil.format("{} run {}.{} error", joinPoint.getTarget().getClass().getName(), targetClass.getName(),methodName));
        }
    }
}
