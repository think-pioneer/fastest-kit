package xyz.thinktest.fastest.core.annotations;

import xyz.thinktest.fastest.core.internal.enhance.methodhelper.PostMethodProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Repeatable(PostMethods.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@After(PostMethodProcess.class)
public @interface PostMethod {
    Class<?> targetClass();
    String method();
    int[] argsIndex() default {};
}
