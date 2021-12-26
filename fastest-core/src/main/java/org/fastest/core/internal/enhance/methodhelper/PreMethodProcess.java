package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.common.exceptions.FastestBasicException;
import org.fastest.common.exceptions.ReflectionException;
import org.fastest.core.annotations.PreMethod;
import org.fastest.core.cnhance.method.JoinPoint;
import org.fastest.utils.ObjectUtil;

import java.lang.reflect.InvocationTargetException;

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
        try {
            Object result = HockMethodTool.invoke(targetClass, methodName, joinPoint.getArgs(), argsIndex);
            joinPoint.setReturn(result);
        }catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionException(ObjectUtil.format("{} run {}.{} error", joinPoint.getTarget().getClass().getName(), targetClass.getName(),methodName), e);
        }catch (ArrayIndexOutOfBoundsException e){
            throw new FastestBasicException(ObjectUtil.format("{}.{} params not enough"), e);
        }
    }
}
