package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Repeatable(PostMethods.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@After
public @interface PostMethod {
    Class<?> targetClass();
    String method();
    int[] argsIndex() default {};
}
