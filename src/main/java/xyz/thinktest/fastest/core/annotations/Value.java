package xyz.thinktest.fastest.core.annotations;

import xyz.thinktest.fastest.core.internal.enhance.fieldhelper.ValueAnnotationProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(ValueAnnotationProcess.class)
@MutexAnnotation({Autowired.class, ValueEntity.class})
public @interface Value {
    String value() default "";
    String key() default "";
    String file() default "";
}
