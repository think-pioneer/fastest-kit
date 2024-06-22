package xyz.think.fastest.core.internal.initialization;

import org.reflections.Reflections;
import xyz.think.fastest.common.exceptions.InitializationException;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.annotations.Pointcut;
import xyz.think.fastest.core.enhance.joinpoint.field.FieldProcessable;
import xyz.think.fastest.core.enhance.joinpoint.method.MethodProcessable;
import xyz.think.fastest.core.internal.enhance.AnnotationGardener;
import xyz.think.fastest.core.internal.enhance.EnhanceFactory;
import xyz.think.fastest.core.internal.scanner.*;
import xyz.think.fastest.utils.reflects.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author: aruba
 * @date: 2022-01-27
 */
@SuppressWarnings("unchecked")
@Component
public class ScannerUnit implements InitializeInternal {
    private final MethodAnnotationProcessCache cacheMethod = MethodAnnotationProcessCache.INSTANCE;
    private final FieldAnnotationProcessCache cacheField = FieldAnnotationProcessCache.INSTANCE;
    private final Reflections reflectionsAnnotation;

    public ScannerUnit(){
        this.reflectionsAnnotation = ReflectionsUnit.INSTANCE.reflections;
    }

    @Override
    public int order() {
        return InitOrderInternalManager.getInstance().consumer(ScannerUnit.class);
    }

    @Override
    public void executor() {
        this.scanner();
    }

    private void scanner(){
        CompletableFuture<Void> methodFuture = CompletableFuture.runAsync(this::methodScanHandler);
        CompletableFuture<Void> fieldFuture = CompletableFuture.runAsync(this::fieldScanHandler);
        try {
            CompletableFuture.allOf(methodFuture, fieldFuture).get();
        }catch (Exception e){
            throw new InitializationException("exec package scan fail", e);
        }
    }

    private void methodScanHandler(){
        Set<Class<? extends MethodProcessable>> methodProcessClasses = reflectionsAnnotation.getSubTypesOf(MethodProcessable.class);
        for(Class<? extends MethodProcessable> clazz : methodProcessClasses){
            if(!ReflectUtil.isInterface(clazz) && !ReflectUtil.isAbstract(clazz)){
                Pointcut pointcut = clazz.getDeclaredAnnotation(Pointcut.class);
                Component component = clazz.getDeclaredAnnotation(Component.class);
                if(Objects.nonNull(pointcut) && Objects.nonNull(component)){
                    Set<Method> methods = this.reflectionsAnnotation.getMethodsAnnotatedWith(pointcut.annotation());
                    for(Method method:methods){
                        MethodAnnotationProcessMeta meta = cacheMethod.get(method);
                        if(Objects.isNull(meta)){
                            meta = new MethodAnnotationProcessMeta();
                        }
                        meta.setMethod(method);
                        boolean before = pointcut.before();
                        boolean after = pointcut.after();
                        Annotation methodAnnotation = method.getAnnotation(pointcut.annotation());
                        if(Objects.nonNull(methodAnnotation)){
                            //注解实现类封装到AnnotationGardener中
                            AnnotationGardener annotationGardener = new AnnotationGardener(methodAnnotation, EnhanceFactory.origin(clazz), pointcut.order());

                            // 对于切面而言，既可以在方法执行前执行，也可以在方法执行后助兴
                            if(before) {
                                meta.setBeforeAnnotation(annotationGardener);
                            }
                            //如果包含after标记，则在方法执行后也要执行
                            if(after) {
                                meta.setAfterAnnotation(annotationGardener);
                            }
                        }
                        cacheMethod.put(method, meta);
                    }
                }
            }
        }
        for(MethodAnnotationProcessMeta entity:cacheMethod.allValue()){
            List<AnnotationGardener> beforeAnnotations = entity.getBeforeAnnotations();
            List<AnnotationGardener> afterAnnotations = entity.getAfterAnnotations();
            beforeAnnotations.sort((o1, o2) -> {
                int nameRet = o1.getAnnotation().getClass().getCanonicalName().compareTo(o2.getAnnotation().getClass().getCanonicalName());
                if (nameRet == 0) {
                    return o1.getIndex().compareTo(o2.getIndex());
                }
                return nameRet;
            });
            afterAnnotations.sort((o1, o2) -> {
                int nameRet = o1.getAnnotation().getClass().getCanonicalName().compareTo(o2.getAnnotation().getClass().getCanonicalName());
                if (nameRet == 0) {
                    return o1.getIndex().compareTo(o2.getIndex());
                }
                return nameRet;
            });
        }
    }

    private void fieldScanHandler(){
        Set<Class<? extends FieldProcessable>> fieldProcessClasses = reflectionsAnnotation.getSubTypesOf(FieldProcessable.class);
        for(Class<? extends FieldProcessable> clazz : fieldProcessClasses){
            if(!ReflectUtil.isInterface(clazz) && !ReflectUtil.isAbstract(clazz)){
                Pointcut pointcut = clazz.getDeclaredAnnotation(Pointcut.class);
                Component component = clazz.getDeclaredAnnotation(Component.class);
                if(Objects.nonNull(pointcut) && Objects.nonNull(component)){
                    Set<Field> fields = this.reflectionsAnnotation.getFieldsAnnotatedWith(pointcut.annotation());
                    for(Field field:fields){
                        FieldAnnotationProcessMeta meta  = cacheField.get(field);
                        if(Objects.isNull(meta)){
                            meta = new FieldAnnotationProcessMeta();
                        }
                        meta.setField(field);
                        Annotation methodAnnotation = field.getAnnotation(pointcut.annotation());
                        if(Objects.nonNull(methodAnnotation)){
                            meta.setBeforeAnnotation(new AnnotationGardener(methodAnnotation, EnhanceFactory.origin(clazz), pointcut.order()));
                        }
                        cacheField.put(field, meta);
                    }
                }
            }
        }
        for(FieldAnnotationProcessMeta meta: cacheField.allValue()){
            List<AnnotationGardener> beforeAnnotations = meta.getBeforeAnnotations();
            beforeAnnotations.sort((o1, o2) -> {
                int nameRet = o1.getAnnotation().getClass().getCanonicalName().compareTo(o2.getAnnotation().getClass().getCanonicalName());
                if (nameRet == 0) {
                    return o1.getIndex().compareTo(o2.getIndex());
                }
                return nameRet;
            });
        }
    }
}
