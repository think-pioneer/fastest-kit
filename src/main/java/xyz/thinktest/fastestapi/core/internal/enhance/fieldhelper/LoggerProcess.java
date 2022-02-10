package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;
import xyz.thinktest.fastestapi.core.internal.enhance.LogFactory;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.core.annotations.LoggerJoin;
import xyz.thinktest.fastestapi.utils.reflects.FieldHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @Date: 2021/11/3
 */
@Pointcut(annotation = LoggerJoin.class)
public class LoggerProcess extends AbstractFieldProcess {

    @Override
    public void process(JoinPoint joinPoint) {
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        Field field = joinPoint.getField();
        Target target = joinPoint.getTarget();
        LoggerJoin loggerJoin = (LoggerJoin) joinPoint.getAnnotation();
        Class<?> fieldType = field.getType();
        if(!FastestLogger.class.equals(fieldType)){
            throw new EnhanceException("@LoggerJoin must annotated org.fastest.logger.FastLogger type");
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
