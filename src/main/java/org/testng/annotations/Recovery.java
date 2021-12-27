package org.testng.annotations;

import org.testng.step.RecoveryStep;

import java.lang.annotation.*;

/**
 * @Date: 2020/12/19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Recovery {
    Class<?> executor() default RecoveryStep.class;
}
