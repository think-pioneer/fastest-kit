package xyz.thinktest.fastestapi.core.annotations;

import xyz.thinktest.fastestapi.http.metadata.HttpMethod;

import java.lang.annotation.*;

/**
 * @Date: 2020/10/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before
@MutexAnnotation(RestMetadata.class)
public @interface RestTemp {
    String name() default "";
    String host();
    String api();
    HttpMethod method();
    String desc() default "";
    boolean save() default false;
    boolean auto() default true;
    boolean sync() default true;
}
