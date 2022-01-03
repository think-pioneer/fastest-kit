package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.common.exceptions.FastestBasicException;
import xyz.thinktest.fastest.common.exceptions.ReflectionException;
import xyz.thinktest.fastest.core.annotations.PostMethod;
import xyz.thinktest.fastest.core.enhance.method.JoinPoint;
import xyz.thinktest.fastest.utils.ObjectUtil;

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
            Object result = HockMethodTool.invoke(targetClass, methodName, joinPoint.getArgs(), argsIndex);
            joinPoint.setReturn(result);
        }catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionException(ObjectUtil.format("{} run {}.{} error", joinPoint.getTarget().getClass().getName(), targetClass.getName(),methodName));
        }catch (ArrayIndexOutOfBoundsException e){
            throw new FastestBasicException(ObjectUtil.format("{}.{} params not enough"), e);
        }
    }
}
