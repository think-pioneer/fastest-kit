package xyz.thinktest.fastestapi.core.annotations;

import java.lang.annotation.*;

/**
 * @Date: 2021/10/24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Component {
//    Class<? extends EasyEnhancer> value() default EasyHandler.class;
}
