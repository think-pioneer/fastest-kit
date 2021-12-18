package org.fastest.core.internal.enhance;

import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.collections4.CollectionUtils;
import org.fastest.common.exceptions.CaptureException;
import org.fastest.common.exceptions.EnhanceException;
import org.fastest.core.annotations.After;
import org.fastest.core.annotations.Before;
import org.fastest.core.annotations.Capture;
import org.fastest.core.internal.enhance.methodhelper.MethodProcess;
import org.fastest.utils.ObjectUtil;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/3/29
 */

public class EasyHandler implements EasyEnhancerable {
    @Override
    public Object intercept(Object self, Method method, Object[] args, MethodProxy methodProxy) {
        Capture capture = method.getAnnotation(Capture.class);
        try {
            return proxy(self, method, args, methodProxy);
        }catch (Throwable c){
            CaptureException cause = new CaptureException(c);
            Class<?> clazz = method.getDeclaringClass();
            Logger logger = LogFactory.getLogger(clazz.getSimpleName());
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

    private Object proxy(Object self, Method method, Object[] args, MethodProxy methodProxy){
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
            MethodProcess methodProcess = new MethodProcess(beforeAnnotations);
            methodProcess.process(self, method, args);
        }
        Object result = invoke(method, methodProxy, self, args);
        if(CollectionUtils.isNotEmpty(afterAnnotations)){
            MethodProcess methodProcess = new MethodProcess(afterAnnotations);
            methodProcess.process(self, method, args);
        }
        return result;
    }

    private Object invoke(Method method, MethodProxy methodProxy, Object object, Object[] args){
        try {
            return methodProxy.invokeSuper(object, args);
        }catch (Throwable cause){
            throw new EnhanceException(ObjectUtil.format("run method error: {}->{}",method.getDeclaringClass().getName(), method.getName()), cause);
        }
    }
}
