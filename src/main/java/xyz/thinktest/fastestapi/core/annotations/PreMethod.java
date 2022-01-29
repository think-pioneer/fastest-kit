package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Repeatable(PreMethods.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before
public @interface PreMethod {
    Class<?> targetClass();
    String method();
    int[] argsIndex() default {};
}
