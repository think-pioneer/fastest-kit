package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.core.annotations.MultipleInstance;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.FieldProcessable;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Date: 2021/10/29
 */
public class ComponentProcess implements FieldProcessable {
    private final InstanceCache instanceCache = InstanceCache.CACHE;
    private final Class<?> clazz;
    private final Class<?>[] argumentTypes;
    private final Object[] arguments;
    private final boolean isOrigin;

    public ComponentProcess(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments, boolean isOrigin){
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
        MultipleInstance multipleInstance = clazz.getAnnotation(MultipleInstance.class);
        //如果未启用多实例，则默认按照单例来处理  # 优化新能，频繁创建实例影响性能
        if (Objects.isNull(multipleInstance)) {
            instance = instanceCache.get(clazz.hashCode());
        }
        if(Objects.isNull(instance)){
            Component component = clazz.getAnnotation(Component.class);
            if (Objects.nonNull(component) && !isOrigin) {
                if (hasArguments) {
                    instance = EnhanceFactory.enhance(clazz, argumentTypes, arguments);
                } else {
                    instance = EnhanceFactory.enhance(clazz);
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