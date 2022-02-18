package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import xyz.thinktest.fastestapi.core.annotations.Before;

import java.lang.annotation.*;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before
public @interface LogPrint {
}
