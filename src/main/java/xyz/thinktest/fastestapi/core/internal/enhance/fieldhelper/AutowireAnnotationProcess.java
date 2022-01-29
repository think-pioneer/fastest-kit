package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.enhance.constructor.AutowireConstructor;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.core.annotations.Autowired;
import xyz.thinktest.fastestapi.utils.reflects.FieldHelper;

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
@Pointcut(annotation = Autowired.class)
public class AutowireAnnotationProcess extends AbstractFieldProcess {

    @Override
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

    private <T> void exec(JoinPoint joinPoint){
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
                List<AutowireConstructor> params = (List<AutowireConstructor>) object;
                List<Class<?>> paramTypes = new ArrayList<>();
                List<Object> paramObjects = new ArrayList<>();
                params.forEach(param -> {
                    paramTypes.add(param.getType());
                    paramObjects.add(param.getObject());
                });
                ComponentAnnotationProcess componentAnnotationProcess = new ComponentAnnotationProcess(tmpInstance.getClass(), paramTypes.toArray(new Class[0]), paramObjects.toArray(new Object[0]), autowired.isOrigin());
                componentAnnotationProcess.process(new JoinPointImpl(autowired, null, (Target) fieldTargetManger, componentAnnotationProcess));
            } else {
                ComponentAnnotationProcess componentAnnotationProcess = new ComponentAnnotationProcess(fieldType, null, null, autowired.isOrigin());
                componentAnnotationProcess.process(new JoinPointImpl(autowired, null, (Target) fieldTargetManger, componentAnnotationProcess));
            }
            Object fieldInstance = fieldTargetManger.getInstance();
            FieldHelper.getInstance(instance, field).set(fieldInstance);
        }catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            throw new EnhanceException(ObjectUtil.format("Autowired error: {}.{} build instance error", instance.getClass().getName(), field.getName()), e);
        }

    }
}
