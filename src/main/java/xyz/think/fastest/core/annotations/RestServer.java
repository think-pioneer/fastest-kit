package xyz.think.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2020/10/24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RestServer {
    String value();
    String file() default "";
}
