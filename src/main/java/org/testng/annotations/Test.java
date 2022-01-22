package org.testng.annotations;

import org.testng.IRetryAnalyzer;
import org.testng.internal.annotations.DisabledRetryAnalyzer;
import org.testng.step.RecoveryStep;
import org.testng.step.Step;

import java.lang.annotation.*;

/**
 * @Date: 2022/1/1
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Test {
    String[] groups() default {};

    boolean enabled() default true;

    String[] dependsOnGroups() default {};

    String[] dependsOnMethods() default {};

    long timeOut() default 0L;

    long invocationTimeOut() default 0L;

    int invocationCount() default 1;

    int threadPoolSize() default 0;

    int successPercentage() default 100;

    String dataProvider() default "";

    Class<?> dataProviderClass() default Object.class;

    boolean alwaysRun() default false;

    String description() default "";

    Class[] expectedExceptions() default {};

    String expectedExceptionsMessageRegExp() default ".*";

    String suiteName() default "";

    String testName() default "";

    boolean singleThreaded() default false;

    Class<? extends IRetryAnalyzer> retryAnalyzer() default DisabledRetryAnalyzer.class;

    boolean skipFailedInvocations() default false;

    boolean ignoreMissingDependencies() default false;

    int priority() default 0;

    CustomAttribute[] attributes() default {};

    /**
     * 是否对step进行恢复操作
     */
    boolean recovery() default true;

    /**
     *需要进行恢复操作的step类
     * 如果为Step.class则对测试类下的所有step进行恢复操作
     */
    Class<?>[] stepType() default Step.class;

    /**
     *进行step操作的执行类
     */
    Class<?> executor() default RecoveryStep.class;
}
