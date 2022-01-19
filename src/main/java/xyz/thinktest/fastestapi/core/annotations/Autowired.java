package xyz.thinktest.fastestapi.core.annotations;

import xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper.AutowireAnnotationProcess;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(AutowireAnnotationProcess.class)
@MutexAnnotation({Value.class, ValueEntity.class})
public @interface Autowired {
    Class<?> targetClass() default Autowired.class;
    String method() default "";
    boolean isOrigin() default false;
}
