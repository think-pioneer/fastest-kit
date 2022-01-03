package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastest.common.exceptions.EnhanceException;
import xyz.thinktest.fastest.core.annotations.LoggerJoin;
import xyz.thinktest.fastest.core.enhance.field.JoinPoint;
import xyz.thinktest.fastest.core.internal.enhance.LogFactory;
import xyz.thinktest.fastest.utils.ObjectUtil;
import xyz.thinktest.fastest.utils.reflects.FieldHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @Date: 2021/11/3
 */
public class LoggerAnnotationProcess extends AbstractFieldProcess {

    @Override
    public void process(JoinPoint joinPoint) {
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        Field field = joinPoint.getField();
        Target target = joinPoint.getTarget();
        LoggerJoin loggerJoin = (LoggerJoin) joinPoint.getAnnotation();
        Class<?> fieldType = field.getType();
        if(!"org.fastest.logger.FastLogger".equals(fieldType.getName())){
            throw new EnhanceException("@LoggerSlf4j must annotated org.fastest.logger.FastLogger type");
        }
        if(!Modifier.isStatic(field.getModifiers())){
            throw new EnhanceException(ObjectUtil.format("Field {} is not static", field.getName()));
        }
        String name = loggerJoin.value();
        if(StringUtils.isEmpty(name)){
            name = field.getDeclaringClass().getSimpleName();
        }
        FieldHelper.getInstance(target.getInstance(), field).set(LogFactory.getLogger(name));
    }
}
