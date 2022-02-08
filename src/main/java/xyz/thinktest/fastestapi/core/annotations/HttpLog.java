package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * @author: aruba
 * @date: 2022-02-08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpLog {
    boolean showRequestLog() default true;
    boolean showResponseLog() default false;
}
