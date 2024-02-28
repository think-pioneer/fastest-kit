package xyz.think.fastest.core.internal.enhance.fieldhelper;

import xyz.think.fastest.common.exceptions.EnhanceException;
import xyz.think.fastest.core.annotations.LoggerJoin;
import xyz.think.fastest.core.annotations.Pointcut;
import xyz.think.fastest.core.enhance.joinpoint.Target;
import xyz.think.fastest.core.enhance.joinpoint.field.JoinPoint;
import xyz.think.fastest.core.internal.enhance.LogFactory;
import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.utils.reflects.FieldHelper;
import xyz.think.fastest.utils.string.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 自动注入FastestLogger对象到变量中。log类型为xyz.think.fastest.logger.FastestLogger
 * @see FastestLogger
 * @Date: 2021/11/3
 */
@Pointcut(annotation = LoggerJoin.class, before = true)
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
            throw new EnhanceException(StringUtils.format("Field {0} is not static", field.getName()));
        }
        String name = loggerJoin.value();
        if(StringUtils.isEmpty(name)){
            name = field.getDeclaringClass().getSimpleName();
        }
        FieldHelper.getInstance(target.getInstance(), field).set(LogFactory.getLogger(name));
    }
}
