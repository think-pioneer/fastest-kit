package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
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
    boolean before() default true;
    boolean after() default false;
}
