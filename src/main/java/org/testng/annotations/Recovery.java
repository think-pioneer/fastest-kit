package org.testng.annotations;

import org.testng.step.RecoveryExecutorStep;
import org.testng.step.Step;

import java.lang.annotation.*;

/**
 * @author: aruba
 * @date: 2022-02-19
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Recovery {
    /**
     * 是否对step进行恢复操作
     */
    boolean recovery() default true;

    /**
     * 超时时间
     */
    long timeout() default 60 * 1000;

    /**
     *需要进行恢复操作的step类
     * 如果为Step.class则对测试类下的所有step进行恢复操作
     */
    Class<?>[] stepType() default Step.class;

    /**
     *进行step操作的执行类
     */
    Class<?> executor() default RecoveryExecutorStep.class;

    /**
     * use thread.stop() exit recovery task if task timeout.
     */
    boolean forceStop() default false;
}
