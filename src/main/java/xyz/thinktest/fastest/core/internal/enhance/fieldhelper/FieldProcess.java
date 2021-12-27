package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastest.core.annotations.Before;
import xyz.thinktest.fastest.core.annotations.Component;
import xyz.thinktest.fastest.core.annotations.MutexAnnotation;
import xyz.thinktest.fastest.core.cnhance.field.FieldAnnotationProcessable;
import xyz.thinktest.fastest.core.internal.enhance.AnnotationGardener;
import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastest.core.internal.tool.AnnotationTool;
import xyz.thinktest.fastest.utils.reflects.ReflectUtil;

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
            FieldAnnotationProcessable process = (FieldAnnotationProcessable) EnhanceFactory.origin(ReflectUtil.get(hockAnnotation, "value"));
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
