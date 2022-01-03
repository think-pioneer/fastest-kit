package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastest.common.exceptions.EnhanceException;
import xyz.thinktest.fastest.core.annotations.Autowired;
import xyz.thinktest.fastest.core.enhance.field.JoinPoint;
import xyz.thinktest.fastest.core.enhance.constructor.ConstructorProperty;
import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastest.utils.ObjectUtil;
import xyz.thinktest.fastest.utils.reflects.FieldHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2021/10/29
 */
@SuppressWarnings("unchecked")
public class AutowireAnnotationProcess extends AbstractFieldProcess {

    public void process(JoinPoint joinPoint){
        Class<?> declaringClass = joinPoint.getField().getDeclaringClass();
        Class<?> fieldType = joinPoint.getField().getType();
        if(checkCircularReference(declaringClass, fieldType)){
            throw new EnhanceException(ObjectUtil.format("exist circular reference: {}->{}", declaringClass, fieldType));
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
            if(!"".equals(autowired.method().trim()) && !Autowired.class.equals(autowired.targetClass())){
                Object tmpInstance = EnhanceFactory.origin(fieldType);
                Method fieldInstanceConstructorParams = tmpInstance.getClass().getDeclaredMethod(autowired.method());
                Object object = fieldInstanceConstructorParams.invoke(tmpInstance);
                if(!(object instanceof Map<?, ?>) && ObjectUtil.checkMapElementType(object, Class.class, Object.class)){
                    throw new EnhanceException(ObjectUtil.format("Constructor error:{}.{} return type export Map<Class, Object>, got {}", field.getType().getName(), fieldInstanceConstructorParams.getName(), object.getClass().getSimpleName()));
                }
                List<ConstructorProperty> params = (List<ConstructorProperty>) object;
                List<Class<?>> paramTypes = new ArrayList<>();
                List<Object> paramObjects = new ArrayList<>();
                params.forEach(param -> {
                    paramTypes.add(param.getType());
                    paramObjects.add(param.getObject());
                });
                ComponentAnnotationProcess componentAnnotationProcess = new ComponentAnnotationProcess(tmpInstance.getClass(), paramTypes.toArray(new Class[0]), paramObjects.toArray(new Object[0]), autowired.isOrigin());
                componentAnnotationProcess.process(new JoinPointImpl(autowired, null, fieldTargetManger, componentAnnotationProcess));
            } else {
                ComponentAnnotationProcess componentAnnotationProcess = new ComponentAnnotationProcess(fieldType, null, null, autowired.isOrigin());
                componentAnnotationProcess.process(new JoinPointImpl(autowired, null, fieldTargetManger, componentAnnotationProcess));
            }
            Object fieldInstance = fieldTargetManger.getInstance();
            FieldHelper.getInstance(instance, field).set(fieldInstance);
        }catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            throw new EnhanceException(ObjectUtil.format("Autowired error: {}.{} build instance error", instance.getClass().getName(), field.getName()), e);
        }

    }
}
