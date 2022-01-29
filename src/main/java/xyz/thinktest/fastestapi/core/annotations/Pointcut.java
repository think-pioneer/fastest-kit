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
    int index() default 0;
}
