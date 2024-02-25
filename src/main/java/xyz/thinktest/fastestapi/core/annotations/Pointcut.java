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
    Class<? extends Annotation> annotation();
    @Deprecated
    int index() default 0;

    /**
     * 处理切面前执行
     */
    boolean before() default false;

    /**
     * 处理切面后执行
     */
    boolean after() default false;
}
