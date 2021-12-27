package org.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Capture {
    boolean isThrow() default true;
    String message() default "";
    Class<?> exception() default Throwable.class;
}
