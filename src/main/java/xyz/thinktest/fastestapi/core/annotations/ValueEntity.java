package xyz.thinktest.fastestapi.core.annotations;

import xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper.ValueEntityAnnotationProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/11/14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(ValueEntityAnnotationProcess.class)
@MutexAnnotation({Autowired.class, Value.class})
public @interface ValueEntity {
    String key() default "";
    String file();
}
