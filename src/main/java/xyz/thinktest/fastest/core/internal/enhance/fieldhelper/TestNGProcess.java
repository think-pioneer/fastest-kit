package xyz.thinktest.fastest.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastest.core.enhance.joinpoint.Target;

import java.lang.reflect.Field;

/**
 * @Date: 2021/10/31
 */
public class TestNGProcess<T> {
    private final Class<?> clazz;

    public TestNGProcess(Class<?> clazz){
        this.clazz = clazz;
    }

    public void process(Target<T> manger) {
        for (Field field : clazz.getDeclaredFields()) {
            new FieldProcess<>(manger, field).process();
        }
    }
}
