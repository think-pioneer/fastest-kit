package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MutexAnnotation({Autowired.class, ValueEntity.class})
public @interface Value {
    String value() default "";
    String key() default "";
    String file() default "";
}
