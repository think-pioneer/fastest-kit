package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.core.annotations.Component;
import org.fastest.core.annotations.MutexAnn;
import org.fastest.core.internal.ReflectTool;
import org.fastest.core.internal.enhance.AnnotationGardener;
import org.fastest.core.internal.enhance.EnhanceFactory;
import org.fastest.core.internal.tool.AnnotationTool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
        for (AnnotationGardener gardener : this.annotations) {
            Annotation hockAnnotation = gardener.getHockAnnotation();
            Annotation annotation = gardener.getAnnotation();
            processType = (Class<?>) ReflectTool.get(hockAnnotation, "value");
            MethodAnnotationProcessable process = (MethodAnnotationProcessable) EnhanceFactory.origin(processType);
            if(AnnotationTool.hasAnnotation(method.getDeclaringClass(), Component.class)) {
                MutexAnn mutexAnn = annotation.annotationType().getDeclaredAnnotation(MutexAnn.class);
                if(Objects.nonNull(mutexAnn)) {
                    AnnotationTool.checkIsOnly(method, annotation.getClass(), mutexAnn.value());
                }
                process.process(new JoinPointImpl(annotation, method, args, target, process));
            }
        }
    }
}
