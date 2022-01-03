package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.core.annotations.Component;
import xyz.thinktest.fastest.core.annotations.MutexAnnotation;
import xyz.thinktest.fastest.core.enhance.method.MethodAnnotationProcessable;
import xyz.thinktest.fastest.core.enhance.method.MethodReturn;
import xyz.thinktest.fastest.core.internal.enhance.AnnotationGardener;
import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastest.core.internal.tool.AnnotationTool;
import xyz.thinktest.fastest.utils.reflects.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/11/29
 */
public class MethodProcess {
    private final List<AnnotationGardener> annotations;

    public MethodProcess(List<AnnotationGardener> annotations){
        this.annotations = annotations;
    }

    public void process(Object target, Method method, Object[] args) {
        Class<?> processType;
        List<Object> methodReturnValues = new ArrayList<>();
        for (AnnotationGardener gardener : this.annotations) {
            Annotation hockAnnotation = gardener.getHockAnnotation();
            Annotation annotation = gardener.getAnnotation();
            processType = ReflectUtil.get(hockAnnotation, "value");
            MethodAnnotationProcessable process = (MethodAnnotationProcessable) EnhanceFactory.origin(processType);
            if(AnnotationTool.hasAnnotation(method.getDeclaringClass(), Component.class)) {
                MutexAnnotation mutexAnnotation = annotation.annotationType().getDeclaredAnnotation(MutexAnnotation.class);
                if(Objects.nonNull(mutexAnnotation)) {
                    AnnotationTool.checkIsOnly(method, annotation.getClass(), mutexAnnotation.value());
                }
                JoinPointImpl joinPoint = new JoinPointImpl(annotation, method, args, target, process);
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
