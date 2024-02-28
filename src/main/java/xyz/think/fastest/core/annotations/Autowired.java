package xyz.think.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MutexAnnotation({Value.class, ValueEntity.class})
public @interface Autowired {
    String constructor() default "";
    boolean isOrigin() default false;
}
