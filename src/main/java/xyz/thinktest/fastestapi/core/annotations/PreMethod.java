package xyz.thinktest.fastestapi.core.annotations;

import xyz.thinktest.fastestapi.core.internal.enhance.methodhelper.PreMethodProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Repeatable(PreMethods.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(PreMethodProcess.class)
public @interface PreMethod {
    Class<?> targetClass();
    String method();
    int[] argsIndex() default {};
}
