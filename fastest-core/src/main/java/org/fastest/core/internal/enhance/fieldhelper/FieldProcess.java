package org.fastest.core.internal.enhance.fieldhelper;

import org.fastest.core.annotations.Before;
import org.fastest.core.annotations.Component;
import org.fastest.core.annotations.MutexAnnotation;
import org.fastest.core.internal.ReflectTool;
import org.fastest.core.internal.enhance.AnnotationGardener;
import org.fastest.core.internal.enhance.EnhanceFactory;
import org.fastest.core.internal.tool.AnnotationTool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/10/31
 */
class FieldProcess {
    private final Target target;
    private final Field field;

    protected FieldProcess(Target target, Field field){
        this.target = target;
        this.field = field;
    }
    public void process() {
        exec();
    }

    private void exec(){
        if(Modifier.isFinal(field.getModifiers())){
            return;
        }
        field.setAccessible(true);
        Annotation[] fieldDeclaredAnnotations = field.getDeclaredAnnotations();
        List<AnnotationGardener> annotationGardenerList = new ArrayList<>();
        for(Annotation annotation:fieldDeclaredAnnotations){
            Before before = annotation.annotationType().getDeclaredAnnotation(Before.class);
            if(Objects.nonNull(before)){
                annotationGardenerList.add(new AnnotationGardener(annotation, before));
            }
        }
        for (AnnotationGardener gardener : annotationGardenerList) {
            Annotation hockAnnotation = gardener.getHockAnnotation();
            Annotation annotation = gardener.getAnnotation();
            Class<?> processType = (Class<?>) ReflectTool.get(hockAnnotation, "value");
            FieldAnnotationProcessable process = (FieldAnnotationProcessable) EnhanceFactory.origin(processType);
            if (AnnotationTool.hasAnnotation(field.getDeclaringClass(), Component.class)) {
                MutexAnnotation mutexAnnotation = annotation.annotationType().getDeclaredAnnotation(MutexAnnotation.class);
                if(Objects.nonNull(mutexAnnotation)){
                    AnnotationTool.checkIsOnly(field, annotation.getClass(), mutexAnnotation.value());
                }
                process.process(new JoinPointImpl(annotation, field, target, process));
            }
        }
    }
}
