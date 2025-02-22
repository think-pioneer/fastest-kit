package xyz.think.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * 在方法之前执行，可以进行临时的参数注入、log打印等等一系列前置操作
 * @Date: 2021/10/24
 */
@Repeatable(PreMethods.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreMethod {

    /**
     * 具体执行pre method的类
     */
    Class<?> targetClass();

    /**
     * 具体执行pre method的方法，如果有参数需要是被注解方法入参的类型中一个或全部
     */
    String method();

    /**
     * 从被注解方法的入参中选取参数，元素是被注解方法入参的位置。例如：argsIndex = {1,1}，即连续选取被注解方法<br>的第一个参数，作为premethod的入参premethod(arg1, arg1)。
     */
    int[] argsIndex() default {};
}
