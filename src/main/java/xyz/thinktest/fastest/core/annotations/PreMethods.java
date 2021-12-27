package xyz.thinktest.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreMethods {
    PreMethod[] value();
}
