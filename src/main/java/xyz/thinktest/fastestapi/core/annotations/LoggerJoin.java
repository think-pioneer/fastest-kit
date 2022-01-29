package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/11/2
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before
public @interface LoggerJoin {
    String value() default "";
}
