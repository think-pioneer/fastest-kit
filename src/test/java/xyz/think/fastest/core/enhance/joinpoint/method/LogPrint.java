package xyz.think.fastest.core.enhance.joinpoint.method;

import java.lang.annotation.*;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogPrint {
}
