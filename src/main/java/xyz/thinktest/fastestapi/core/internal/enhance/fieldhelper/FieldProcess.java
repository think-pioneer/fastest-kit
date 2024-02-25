package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.core.annotations.MutexAnnotation;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.FieldProcessable;
import xyz.thinktest.fastestapi.core.internal.enhance.AnnotationGardener;
import xyz.thinktest.fastestapi.core.internal.scanner.FieldAnnotationProcessCache;
import xyz.thinktest.fastestapi.core.internal.scanner.FieldAnnotationProcessMeta;
import xyz.thinktest.fastestapi.core.internal.tool.AnnotationTool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @Date: 2021/10/31
 */
class FieldProcess {
    private final Target target;
    private final Field field;
    private final FieldAnnotationProcessCache cache;

    protected FieldProcess(Target target, Field field){
        this.target = target;
        this.field = field;
        this.cache = FieldAnnotationProcessCache.INSTANCE;
    }
    public void process() {
        exec();
    }

    private void exec(){
        if(Modifier.isFinal(field.getModifiers())){
            return;
        }
        field.setAccessible(true);
        FieldAnnotationProcessMeta meta = cache.get(field);
        if(Objects.isNull(meta)){
            return;
        }
        // 获取字段的前置注解，并执行前置注解对应的功能实现类
        for(AnnotationGardener annotationGardener:meta.getBeforeAnnotations()){
            FieldProcessable process = (FieldProcessable) annotationGardener.getProcess();
            Annotation annotation = annotationGardener.getAnnotation();
            if (AnnotationTool.hasAnnotation(field.getDeclaringClass(), Component.class)) {
                MutexAnnotation mutexAnnotation = annotation.annotationType().getDeclaredAnnotation(MutexAnnotation.class);
                if(Objects.nonNull(mutexAnnotation)){
                    AnnotationTool.checkIsOnly(field, annotation.getClass(), mutexAnnotation.value());
                }
                process.process(new JoinPointMeta(annotation, field, target, process));
            }
        }
    }
}
