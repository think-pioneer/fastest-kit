package org.fastest.core.internal.enhance;

import org.fastest.common.exceptions.ReflectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @Date: 2021/11/6
 */
public final class FieldTool {
    private static final Logger logger = LoggerFactory.getLogger(FieldTool.class);
    private FieldTool(){}

    public static void set(Field field, Object instance, Object value){
        Class<?> decClass = null;
        Class<?> fieldType = null;
        String fieldName = null;
        try{
            decClass = field.getDeclaringClass();
            fieldType = field.getType();
            fieldName = field.getName();
            if(Modifier.isStatic(field.getModifiers())){
                field.set(null, value);
            }else {
                field.set(instance, value);
            }
        }catch (IllegalAccessException e){
            logger.error("{}.{} {} set value fail.", decClass.getName(), fieldType.getSimpleName(), fieldName);
        }
    }

    public static Object get(Object object, String name){
        try{
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e){
            throw new ReflectionException(e.getMessage(),e);
        }
    }
}
