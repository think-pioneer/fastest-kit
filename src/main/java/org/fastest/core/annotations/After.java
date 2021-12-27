package org.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/12/4
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface After {
    Class<?> value();
}
