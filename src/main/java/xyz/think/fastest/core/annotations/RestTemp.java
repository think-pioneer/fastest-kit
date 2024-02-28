package xyz.think.fastest.core.annotations;

import xyz.think.fastest.http.metadata.HttpMethod;

import java.lang.annotation.*;

/**
 * @Date: 2020/10/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MutexAnnotation(RestMetadata.class)
public @interface RestTemp {
    /**
     * http api名
     */
    String name() default "";

    /**
     * http 域名
     */
    String host();

    /**
     * http 接口
     */
    String api();

    /**
     * http 请求方法
     */
    HttpMethod method();

    /**
     * 接口描述信息
     */
    String desc() default "";

    /**
     * 是否保存，默认不保存。全局参数（fastest.rest.temp.save）和该参数任意一个true都将保存。
     */
    boolean save() default false;

    /**
     * 是否自动发起请求，默认是
     */
    boolean auto() default true;

    /**
     * 是否使用同步请求，默认是
     */
    boolean sync() default true;
}
