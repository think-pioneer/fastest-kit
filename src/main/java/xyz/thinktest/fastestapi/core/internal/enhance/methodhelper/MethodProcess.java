package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.core.annotations.MutexAnnotation;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.MethodProcessable;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.MethodReturn;
import xyz.thinktest.fastestapi.core.internal.enhance.AnnotationGardener;
import xyz.thinktest.fastestapi.core.internal.tool.AnnotationTool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/11/29
 */
public class MethodProcess {
    private final List<AnnotationGardener> annotationGardeners;

    public MethodProcess(List<AnnotationGardener> annotationGardeners){
        this.annotationGardeners = annotationGardeners;
    }

    public void process(Target target, Method method, Object[] args) {
        List<Object> methodReturnValues = new ArrayList<>();
        for (AnnotationGardener gardener : this.annotationGardeners) {
            Annotation annotation = gardener.getAnnotation();
            MethodProcessable process = (MethodProcessable) gardener.getProcess();
            if(AnnotationTool.hasAnnotation(method.getDeclaringClass(), Component.class)) {
                MutexAnnotation mutexAnnotation = annotation.annotationType().getDeclaredAnnotation(MutexAnnotation.class);
                if(Objects.nonNull(mutexAnnotation)) {
                    AnnotationTool.checkIsOnly(method, annotation.getClass(), mutexAnnotation.value());
                }
                JoinPointMeta joinPoint = new JoinPointMeta(annotation, method, args, target, process);
                process.process(joinPoint);
                methodReturnValues.add(joinPoint.getReturnValue());
            }
        }

        for(Object arg:args){
            if(arg instanceof MethodReturn){
                MethodReturn methodReturn = (MethodReturn) arg;
                methodReturn.setReturnValue(methodReturnValues);
            }
        }
    }
}
