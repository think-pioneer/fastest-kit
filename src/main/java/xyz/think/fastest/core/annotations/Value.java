package xyz.think.fastest.core.annotations;

import java.lang.annotation.*;

/**
 * 从yml/yaml文件中读取配置文件的值，并赋值给被注解变量。如果多个配置文件有相同的变量名，则会取最后加载的变量(加载规则由jvm控制)
 * @Date: 2021/10/24
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MutexAnnotation({Autowired.class, ValueEntity.class})
public @interface Value {
    /**
     * 配置文件中的key（和key的区别，不用输入字段名）,仅仅时为了在使用注解时，不用额外指定注解属性。
     */
    String value() default "";

    /**
     * 配置文件中的key（和value的区别，需要输入字段名）
     */
    String key() default "";

    /**
     * 指定了file后将会从该file读取配置信息
     */
    String file() default "";
}
