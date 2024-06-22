package xyz.think.fastest.core.internal.enhance.fieldhelper;

import xyz.think.fastest.common.exceptions.EnhanceException;
import xyz.think.fastest.core.annotations.Autowired;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.annotations.Pointcut;
import xyz.think.fastest.core.enhance.constructor.ConstructorProperty;
import xyz.think.fastest.core.enhance.joinpoint.Target;
import xyz.think.fastest.core.enhance.joinpoint.field.JoinPoint;
import xyz.think.fastest.core.internal.enhance.EnhanceFactory;
import xyz.think.fastest.utils.reflects.FieldHelper;
import xyz.think.fastest.utils.string.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 处理自动装载
 * 只有使用了Autowired注解的字段才会进行自动装配
 * @Date: 2021/10/29
 */
@Component
@SuppressWarnings("unchecked")
@Pointcut(annotation = Autowired.class, before = true)
public class AutowireProcess extends AbstractFieldProcess {

    @Override
    public void process(JoinPoint joinPoint){
        Class<?> declaringClass = joinPoint.getField().getDeclaringClass();
        Class<?> fieldType = joinPoint.getField().getType();
        if(checkCircularReference(declaringClass, fieldType)){
            throw new EnhanceException(StringUtils.format("exist circular reference: {0}->{1}", declaringClass, fieldType));
        }
        this.exec(joinPoint);
    }

    private boolean checkCircularReference(Class<?> declaringClass, Class<?> fieldType){
        for(Field fieldTypeOfField : fieldType.getDeclaredFields()){
            if(declaringClass.equals(fieldTypeOfField.getType())){
                return true;
            }
        }
        return false;
    }

    private void exec(JoinPoint joinPoint){
        Field field = joinPoint.getField();
        Target target = joinPoint.getTarget();
        Autowired autowired = (Autowired) joinPoint.getAnnotation();
        Object instance = target.getInstance();
        Class<?> fieldType = field.getType();
        try{
            Target fieldTargetManger = new Target();
            if(!"".equals(autowired.constructor().trim())){
                Object tmpInstance = EnhanceFactory.origin(fieldType);
                Method fieldInstanceConstructorParams = tmpInstance.getClass().getDeclaredMethod(autowired.constructor());
                Object constructorPropertyObj = fieldInstanceConstructorParams.invoke(tmpInstance);
                if(constructorPropertyObj == null){
                    throw new EnhanceException(StringUtils.format("Constructor error:{0}.{1} return type export ConstructorProperty type, got null", field.getType().getName(), fieldInstanceConstructorParams.getName()));
                }
                if(!(constructorPropertyObj instanceof ConstructorProperty)){
                    throw new EnhanceException(StringUtils.format("Constructor error:{0}.{1} return type export ConstructorProperty type, got {}", field.getType().getName(), fieldInstanceConstructorParams.getName(), constructorPropertyObj.getClass().getSimpleName()));
                }
                ConstructorProperty constructorProperty = (ConstructorProperty) constructorPropertyObj;
                ComponentProcess componentAnnotationProcess = new ComponentProcess(tmpInstance.getClass(), constructorProperty.getTypes().toArray(new Class[0]), constructorProperty.getObjects().toArray(new Object[0]), autowired.isOrigin());
                componentAnnotationProcess.process(new JoinPointMeta(autowired, null, fieldTargetManger, componentAnnotationProcess));
            } else {
                ComponentProcess componentAnnotationProcess = new ComponentProcess(fieldType, null, null, autowired.isOrigin());
                componentAnnotationProcess.process(new JoinPointMeta(autowired, null, fieldTargetManger, componentAnnotationProcess));
            }
            Object fieldInstance = fieldTargetManger.getInstance();
            FieldHelper.getInstance(instance, field).set(fieldInstance);
        }catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            throw new EnhanceException(StringUtils.format("Autowired error: {0}.{1} build instance error", instance.getClass().getName(), field.getName()), e);
        }

    }
}
