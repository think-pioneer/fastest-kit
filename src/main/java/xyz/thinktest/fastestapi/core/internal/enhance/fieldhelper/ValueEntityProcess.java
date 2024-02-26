package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.common.json.JSONFactory;
import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.annotations.ValueEntity;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.utils.files.YamlUtil;
import xyz.thinktest.fastestapi.utils.reflects.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * 用于将配置文件的内容直接赋值给实体对象
 * @Date: 2021/11/27
 */
@SuppressWarnings("unchecked")
@Pointcut(annotation = ValueEntity.class, before = true)
public class ValueEntityProcess extends AbstractFieldProcess {

    @Override
    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        Field field = joinPoint.getField();
        ValueEntity valueEntity = (ValueEntity) joinPoint.getAnnotation();
        try {
            String key = valueEntity.key().trim();
            String fileName = valueEntity.file().trim();
            Type entityType = field.getGenericType();
            Class<?> entityClazz = field.getType();
            Object obj;
            if(entityClazz.getTypeName().endsWith("List")){
                if(StringUtils.isEmpty(fileName)){
                    throw new EnhanceException("List mode need file name");
                }
                List<Object> list = YamlUtil.getAll(fileName, key);
                Class<?> eleType = ReflectUtil.getCollectionGenericRealType(entityType);
                obj = JSONFactory.stringToObject(JSONFactory.objectToJson(list).toString(), List.class, eleType);
            }else {
                HashMap<String, ?> hashMap = (HashMap<String, ?>) YamlUtil.get(fileName, key);
                obj = EnhanceFactory.origin(entityClazz);
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                beanUtilsBean.populate(obj, hashMap);
            }
            field.set(joinPoint.getTarget().getInstance(), obj);
        }catch (IllegalAccessException | InvocationTargetException e){
            throw new EnhanceException("set entity value error", e);
        }
    }
}
