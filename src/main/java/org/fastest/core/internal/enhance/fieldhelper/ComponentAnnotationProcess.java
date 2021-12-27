package org.fastest.core.internal.enhance.fieldhelper;

import org.fastest.core.annotations.Component;
import org.fastest.core.annotations.Singleton;
import org.fastest.core.cnhance.field.JoinPoint;
import org.fastest.core.cnhance.field.FieldAnnotationProcessable;
import org.fastest.core.internal.enhance.EasyHandler;
import org.fastest.core.internal.enhance.EnhanceFactory;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Date: 2021/10/29
 */
public class ComponentAnnotationProcess implements FieldAnnotationProcessable {
    private final InstanceCache instanceCache = InstanceCache.getInstance();
    private final Class<?> clazz;
    private final Class<?>[] argumentTypes;
    private final Object[] arguments;
    private final boolean isOrigin;

    public ComponentAnnotationProcess(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments, boolean isOrigin){
        this.clazz = clazz;
        this.argumentTypes = argumentTypes;
        this.arguments = arguments;
        this.isOrigin = isOrigin;
    }

    @Override
    public void process(JoinPoint joinPoint) {
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        Target target = joinPoint.getTarget();
        Object instance = null;
        boolean hasArguments = Objects.nonNull(argumentTypes) && Objects.nonNull(arguments);
        Singleton singleton = clazz.getDeclaredAnnotation(Singleton.class);
        if (Objects.nonNull(singleton)) {
            instance = instanceCache.get(clazz.hashCode());
        }
        if(Objects.isNull(instance)){
            Component component = clazz.getDeclaredAnnotation(Component.class);
            if (Objects.nonNull(component) && !isOrigin) {
                Class<?> callBackType = EasyHandler.class;
                if (hasArguments) {
                    instance = EnhanceFactory.enhance(clazz, argumentTypes, arguments, callBackType);
                } else {
                    instance = EnhanceFactory.enhance(clazz, callBackType);
                }
            } else {
                if (hasArguments) {
                    instance = EnhanceFactory.origin(clazz, argumentTypes, arguments);
                } else {
                    instance = EnhanceFactory.origin(clazz);
                }
            }
            instanceCache.put(clazz.hashCode(), instance);
        }
        target.setInstance(instance);
        for(Field field1:clazz.getDeclaredFields()){
            new FieldProcess(target, field1).process();
        }
    }
}