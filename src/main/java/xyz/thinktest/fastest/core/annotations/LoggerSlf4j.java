package xyz.thinktest.fastest.core.annotations;

import xyz.thinktest.fastest.core.internal.enhance.fieldhelper.LoggerAnnotationProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/11/2
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(LoggerAnnotationProcess.class)
public @interface LoggerSlf4j {
    String value() default "";
}
