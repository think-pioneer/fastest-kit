package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.common.exceptions.ReflectionException;
import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.annotations.PostMethod;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.JoinPoint;
import xyz.thinktest.fastestapi.utils.string.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @Date: 2021/10/29
 */
@Pointcut(annotation = PostMethod.class, after = true)
public class PostMethodProcess extends AbstractMethodProcess {

    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private <T> void exec(JoinPoint joinPoint){
        PostMethod postMethod = (PostMethod) joinPoint.getAnnotation();
        Class<?> targetClass = postMethod.targetClass();
        String methodName = postMethod.method();
        int[] argsIndex = postMethod.argsIndex();
        try {
            T result = HockMethodTool.invoke(targetClass, methodName, joinPoint.getArgs(), argsIndex);
            joinPoint.setReturn(result);
        }catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionException(StringUtils.format("{0} run {1}.{2} error", joinPoint.getTarget().getClass().getName(), targetClass.getName(),methodName));
        }catch (ArrayIndexOutOfBoundsException e){
            throw new FastestBasicException(StringUtils.format("{0}.{1} params not enough"), e);
        }
    }
}
