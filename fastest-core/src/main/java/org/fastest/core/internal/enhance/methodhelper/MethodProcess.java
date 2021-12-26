package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.core.annotations.Component;
import org.fastest.core.annotations.MutexAnnotation;
import org.fastest.core.internal.enhance.AnnotationGardener;
import org.fastest.core.internal.enhance.EnhanceFactory;
import org.fastest.core.internal.tool.AnnotationTool;
import org.fastest.utils.reflects.ReflectUtil;

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
            processType = ReflectUtil.get(hockAnnotation, "value");
            MethodAnnotationProcessable process = (MethodAnnotationProcessable) EnhanceFactory.origin(processType);
            if(AnnotationTool.hasAnnotation(method.getDeclaringClass(), Component.class)) {
                MutexAnnotation mutexAnnotation = annotation.annotationType().getDeclaredAnnotation(MutexAnnotation.class);
                if(Objects.nonNull(mutexAnnotation)) {
                    AnnotationTool.checkIsOnly(method, annotation.getClass(), mutexAnnotation.value());
                }
                process.process(new JoinPointImpl(annotation, method, args, target, process));
            }
        }
    }
}
