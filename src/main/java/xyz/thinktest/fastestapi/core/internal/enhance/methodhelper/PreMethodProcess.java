package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.JoinPoint;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.common.exceptions.ReflectionException;
import xyz.thinktest.fastestapi.core.annotations.PreMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * @Date: 2021/10/29
 */
@Pointcut(annotation = PreMethod.class)
public class PreMethodProcess extends AbstractMethodProcess {

    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private <T> void exec(JoinPoint joinPoint){
        PreMethod preMethod = (PreMethod) joinPoint.getAnnotation();
        Class<?> targetClass = preMethod.targetClass();
        String methodName = preMethod.method();
        int[] argsIndex = preMethod.argsIndex();
        try {
            T result = HockMethodTool.invoke(targetClass, methodName, joinPoint.getArgs(), argsIndex);
            joinPoint.setReturn(result);
        }catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionException(ObjectUtil.format("{} run {}.{} error", joinPoint.getTarget().getClass().getName(), targetClass.getName(),methodName), e);
        }catch (ArrayIndexOutOfBoundsException e){
            throw new FastestBasicException(ObjectUtil.format("{}.{} params not enough"), e);
        }
    }
}
