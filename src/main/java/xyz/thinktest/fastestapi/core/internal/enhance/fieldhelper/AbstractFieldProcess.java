package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.FieldProcessable;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;

/**
 * 所有字段处理的抽象类
 * process方法作为业务处理的入口，默认不执行任何操作，由具体的实现类实现
 * @Date: 2021/11/3
 */
public abstract class AbstractFieldProcess implements FieldProcessable {
    @Override
    public abstract void process(JoinPoint joinPoint);
}
