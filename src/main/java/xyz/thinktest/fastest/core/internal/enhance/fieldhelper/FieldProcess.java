package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastest.core.annotations.Before;
import xyz.thinktest.fastest.core.annotations.Component;
import xyz.thinktest.fastest.core.annotations.MutexAnnotation;
import xyz.thinktest.fastest.core.enhance.joinpoint.Target;
import xyz.thinktest.fastest.core.enhance.joinpoint.field.FieldAnnotationProcessable;
import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastest.core.internal.tool.AnnotationTool;
import xyz.thinktest.fastest.utils.reflects.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @Date: 2021/10/31
 */
class FieldProcess<T> {
    private final Target<T> target;
    private final Field field;

    protected FieldProcess(Target<T> target, Field field){
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
        for(Annotation annotation:fieldDeclaredAnnotations){
            Before before = annotation.annotationType().getDeclaredAnnotation(Before.class);
            if(Objects.nonNull(before)){
                FieldAnnotationProcessable<T> process = EnhanceFactory.origin(ReflectUtil.get(before, "value"));
                if (AnnotationTool.hasAnnotation(field.getDeclaringClass(), Component.class)) {
                    MutexAnnotation mutexAnnotation = annotation.annotationType().getDeclaredAnnotation(MutexAnnotation.class);
                    if(Objects.nonNull(mutexAnnotation)){
                        AnnotationTool.checkIsOnly(field, annotation.getClass(), mutexAnnotation.value());
                    }
                    process.process(new JoinPointImpl<>(annotation, field, target, process));
                }
            }
        }
    }
}
