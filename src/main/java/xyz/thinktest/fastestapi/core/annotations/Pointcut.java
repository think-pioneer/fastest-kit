package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * 切入点注解
 * 用于标记一个切入点
 * 默认before和after均为false，即不会执行。需要手动开启
 * @author: aruba
 * @date: 2022-01-26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pointcut {
    /**
     * 待切面的注解
     */
    Class<? extends Annotation> annotation();

    /**
     * 多个实现类时的执行顺序，序号一样的直接延后
     */
    int order() default 0;

    /**
     * 标志位，执行时机
     * annotation注解的对象执行前执行
     */
    boolean before() default false;

    /**
     * 标志位，执行时机
     * annotation注解的对象执行后执行
     */
    boolean after() default false;
}
