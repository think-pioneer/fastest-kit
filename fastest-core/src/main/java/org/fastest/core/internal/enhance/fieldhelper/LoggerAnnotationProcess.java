package org.fastest.core.internal.enhance.fieldhelper;

import org.apache.commons.lang3.StringUtils;
import org.fastest.common.exceptions.EnhanceException;
import org.fastest.core.annotations.LoggerSlf4j;
import org.fastest.core.cnhance.field.JoinPoint;
import org.fastest.core.internal.enhance.LogFactory;
import org.fastest.utils.ObjectUtil;
import org.fastest.utils.reflects.FieldHelper;

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
        LoggerSlf4j loggerSlf4j = (LoggerSlf4j) joinPoint.getAnnotation();
        Class<?> fieldType = field.getType();
        if(!"org.fastest.logger.FastLogger".equals(fieldType.getName())){
            throw new EnhanceException("@LoggerSlf4j must annotated org.fastest.logger.FastLogger type");
        }
        if(!Modifier.isStatic(field.getModifiers())){
            throw new EnhanceException(ObjectUtil.format("Field {} is not static", field.getName()));
        }
        String name = loggerSlf4j.value();
        if(StringUtils.isEmpty(name)){
            name = field.getDeclaringClass().getSimpleName();
        }
        FieldHelper.getInstance(target.getInstance(), field).set(LogFactory.getLogger(name));
    }
}
