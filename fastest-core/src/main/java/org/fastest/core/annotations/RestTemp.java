package org.fastest.core.annotations;

import org.fastest.core.internal.enhance.methodhelper.RestTempProcess;
import org.fastest.http.metadata.HttpMethod;

import java.lang.annotation.*;

/**
 * @Date: 2020/10/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(RestTempProcess.class)
@MutexAnn(RestMetadata.class)
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
