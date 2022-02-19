package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2020/10/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MutexAnnotation({RestTemp.class})
public @interface RestMetadata {
    String serverName() default "";
    String apiName();
    String desc() default "";
    boolean auto() default true;
    boolean sync() default true;
    String file() default "";
}
