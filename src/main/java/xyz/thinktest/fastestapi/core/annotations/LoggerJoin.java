package xyz.thinktest.fastestapi.core.annotations;

import xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper.LoggerAnnotationProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/11/2
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(LoggerAnnotationProcess.class)
public @interface LoggerJoin {
    String value() default "";
}
