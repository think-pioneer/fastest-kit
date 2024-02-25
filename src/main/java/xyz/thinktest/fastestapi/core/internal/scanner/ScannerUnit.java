package xyz.thinktest.fastestapi.core.internal.scanner;

import org.reflections.Reflections;
import xyz.thinktest.fastestapi.common.exceptions.InitializationException;
import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.FieldProcessable;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.MethodProcessable;
import xyz.thinktest.fastestapi.core.internal.enhance.AnnotationGardener;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.utils.reflects.ReflectUtil;

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
public class ScannerUnit {
    private final MethodAnnotationProcessCache cacheMethod = MethodAnnotationProcessCache.INSTANCE;
    private final FieldAnnotationProcessCache cacheField = FieldAnnotationProcessCache.INSTANCE;
    private final Reflections reflectionsAnnotation;

    private ScannerUnit(){
        this.reflectionsAnnotation = ReflectionsUnit.INSTANCE.reflections;
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
                if(Objects.nonNull(pointcut)){
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
                            AnnotationGardener annotationGardener = new AnnotationGardener(methodAnnotation, EnhanceFactory.origin(clazz), pointcut.index());

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
                if(Objects.nonNull(pointcut)){
                    Set<Field> fields = this.reflectionsAnnotation.getFieldsAnnotatedWith(pointcut.annotation());
                    for(Field field:fields){
                        FieldAnnotationProcessMeta meta  = cacheField.get(field);
                        if(Objects.isNull(meta)){
                            meta = new FieldAnnotationProcessMeta();
                        }
                        meta.setField(field);
                        Annotation methodAnnotation = field.getAnnotation(pointcut.annotation());
                        if(Objects.nonNull(methodAnnotation)){
                            meta.setBeforeAnnotation(new AnnotationGardener(methodAnnotation, EnhanceFactory.origin(clazz), pointcut.index()));
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

    public static void scan(){
        new ScannerUnit().scanner();
    }
}
