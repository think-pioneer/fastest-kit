package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.core.annotations.PostMethod;
import org.fastest.core.aspect.method.JoinPoint;

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
        HockMethodTool.invoke(targetClass, methodName, joinPoint.getArgs(), argsIndex);
    }
}
