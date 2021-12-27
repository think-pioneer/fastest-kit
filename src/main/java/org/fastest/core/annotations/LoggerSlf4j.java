package org.fastest.core.annotations;

import org.fastest.core.internal.enhance.fieldhelper.LoggerAnnotationProcess;

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
