package org.fastest.core.internal.enhance.fieldhelper;

import java.lang.reflect.Field;

/**
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
