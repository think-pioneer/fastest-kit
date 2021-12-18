package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.core.annotations.PreMethod;
import org.fastest.core.aspect.method.JoinPoint;

/**
 * @Date: 2021/10/29
 */
public class PreMethodProcess extends AbstractMethodProcess {

    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        PreMethod preMethod = (PreMethod) joinPoint.getAnnotation();
        Class<?> targetClass = preMethod.targetClass();
        String methodName = preMethod.method();
        int[] argsIndex = preMethod.argsIndex();
        HockMethodTool.invoke(targetClass, methodName, joinPoint.getArgs(), argsIndex);
    }
}
