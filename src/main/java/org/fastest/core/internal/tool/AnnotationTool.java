package org.fastest.core.internal.tool;

import org.fastest.common.exceptions.EnhanceException;
import org.fastest.utils.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date: 2021/10/31
 */
@SuppressWarnings("unchecked")
public class AnnotationTool {

    /**
     * Some annotations may need to be unique. This method is used to check whether the expected annotation is unique.
     * If successful, the annotation will be returned.
     * @param field Fields to be checked
     * @return
     */
    public static void checkIsOnly(Field field, Class<?> expectType, Class<?>[] excludeTypes){
        List<Class<? extends Annotation>> realAnnType = getRealDeclaredAnnotations(field.getDeclaredAnnotations());
        for(Class<?> excludeType:excludeTypes){
            if(realAnnType.contains(excludeType)){
                throw new EnhanceException(ObjectUtil.format("@{} and @{} cannot annotate {}.{} at the same time", expectType.getSimpleName(), excludeType.getSimpleName(), field.getDeclaringClass().getName(), field.getName()));
            }
        }
    }

    /**
     * Some annotations may need to be unique. This method is used to check whether the expected annotation is unique.
     * If successful, the annotation will be returned.
     * @param method Fields to be checked
     * @return
     */
    public static void checkIsOnly(Method method, Class<?> expectType, Class<?>[] excludeTypes){
        List<Class<? extends Annotation>> realAnnType = getRealDeclaredAnnotations(method.getDeclaredAnnotations());
        for(Class<?> excludeType:excludeTypes){
            if(realAnnType.contains(excludeType)){
                throw new EnhanceException(ObjectUtil.format("@{} and @{} cannot annotate {}.{} at the same time", expectType.getSimpleName(), excludeType.getSimpleName(), method.getDeclaringClass().getName(), method.getName()));
            }
        }
    }

    /**
     * get real annotation
     */
    private static List<Class<? extends Annotation>> getRealDeclaredAnnotations(Annotation[] annotations){
        return Arrays.stream(annotations).map(Annotation::getClass).collect(Collectors.toList());
    }

    /**
     * Check if the annotation exists
     * @param clazz Type to be checked
     * @param expected Expected annotation type
     * @param <T>
     * @return
     */
    public static <T extends Annotation> boolean hasAnnotation(Class<?> clazz, Class<T> expected){
        T annotation = clazz.getAnnotation(expected);
        return Objects.nonNull(annotation);
    }
}
