package xyz.thinktest.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2020/10/24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestServer {
    String value();
    String file() default "";
}
