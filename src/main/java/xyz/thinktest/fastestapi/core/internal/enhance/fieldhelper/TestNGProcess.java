package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;

import java.lang.reflect.Field;

/**
 * testng监听器
 * @Date: 2021/10/31
 */
public class TestNGProcess {
    private final Class<?> clazz;

    public TestNGProcess(Class<?> clazz){
        this.clazz = clazz;
    }

    public void process(Target manger) {
        for (Field field : clazz.getDeclaredFields()) {
            new FieldProcess(manger, field).process();
        }
    }
}
