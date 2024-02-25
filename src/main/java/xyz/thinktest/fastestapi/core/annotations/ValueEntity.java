package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * 通过读取配置文件的内容，将其转换为对象
 * 该注解用在字段上
 * @Date: 2021/11/14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MutexAnnotation({Autowired.class, Value.class})
public @interface ValueEntity {

    /**
     * 配置文件中的前缀，会将后面的值作为实体对象的key。例如：properties中配置user.name。<br>ValueEntity的key为user，则会在被注解的对象中寻找name字段，并赋值给name字段
     */
    String key() default "";

    /**
     * yaml的文件名。必选项
     */
    String file();
}
