package xyz.thinktest.fastestapi.core.internal.enhance;

import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.collections4.CollectionUtils;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.common.exceptions.CaptureException;
import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.core.annotations.After;
import xyz.thinktest.fastestapi.core.annotations.Before;
import xyz.thinktest.fastestapi.core.annotations.Capture;
import xyz.thinktest.fastestapi.core.internal.enhance.methodhelper.MethodProcess;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.logger.FastestLoggerFactory;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.utils.reflects.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/3/29
 */

public class MethodHandler<T> implements MethodEnhancer {
    @Override
    public Object intercept(Object origin, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Capture capture = method.getAnnotation(Capture.class);
        try {
            Target<T> target = new Target<>();
            target.setInstance((T) origin);
            return proxy(target, method, args, methodProxy);
        }catch (Throwable c){
            CaptureException cause = new CaptureException(c);
            Class<?> clazz = method.getDeclaringClass();
            FastestLogger logger = FastestLoggerFactory.getLogger(clazz.getSimpleName());
            if(Objects.nonNull(capture)){
                String message = capture.message();
                message = "".equals(message.trim()) ? cause.getMessage() : message;
                logger.error(message, cause);
                if(capture.isThrow() && capture.exception().isInstance(cause) ){
                    throw cause;
                }
            }else{
                logger.error(cause.getMessage(), cause);
                throw cause;
            }
            return null;
        }
    }

    private Object proxy(Target<T> target, Method method, Object[] args, MethodProxy methodProxy){
        Annotation[] allAnnotation = method.getDeclaredAnnotations();
        List<AnnotationGardener> beforeAnnotations = new ArrayList<>();
        List<AnnotationGardener> afterAnnotations = new ArrayList<>();
        for (Annotation annotation : allAnnotation) {
            Before beforeAnnotation = annotation.annotationType().getDeclaredAnnotation(Before.class);
            After afterAnnotation = annotation.annotationType().getDeclaredAnnotation(After.class);
            if (Objects.nonNull(beforeAnnotation)) {
                beforeAnnotations.add(new AnnotationGardener(annotation, beforeAnnotation));
            }
            if (Objects.nonNull(afterAnnotation)) {
                afterAnnotations.add(new AnnotationGardener(annotation, afterAnnotation));
            }
        }
        if(CollectionUtils.isNotEmpty(beforeAnnotations)) {
            MethodProcess<T> methodProcess = new MethodProcess<>(beforeAnnotations);
            methodProcess.process(target, method, args);
        }
        Object result;
        result = invoke(method, methodProxy, target.getInstance(), args);
        if(CollectionUtils.isNotEmpty(afterAnnotations)){
            MethodProcess<T> methodProcess = new MethodProcess<>(afterAnnotations);
            methodProcess.process(target, method, args);
        }
        return result;
    }

    private Object invoke(Method method, MethodProxy methodProxy, Object object, Object[] args){
        try {
            if(ReflectUtil.isInterface(method) || ReflectUtil.isAbstract(method) || method.isDefault()){
                return null;
            }
            return methodProxy.invokeSuper(object, args);
        }catch (Throwable cause){
            throw new EnhanceException(ObjectUtil.format("run method error: {}->{}",method.getDeclaringClass().getName(), method.getName()), cause);
        }
    }
}
