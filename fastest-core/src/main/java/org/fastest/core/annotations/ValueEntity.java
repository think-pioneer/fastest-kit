package org.fastest.core.annotations;

import org.fastest.core.internal.enhance.fieldhelper.ValueEntityAnnotationProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/11/14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(ValueEntityAnnotationProcess.class)
@MutexAnn({Autowired.class, Value.class})
public @interface ValueEntity {
    String key() default "";
    String file();
}
