package xyz.think.fastest.core.internal.enhance.fieldhelper;

import xyz.think.fastest.core.enhance.joinpoint.Target;

import java.lang.reflect.Field;

/**
 * testng监听器
 * @Date: 2021/10/31
 */
public class TestNGExecutor {
    private final Class<?> clazz;

    public TestNGExecutor(Class<?> clazz){
        this.clazz = clazz;
    }

    public void execute(Target manger) {
        for (Field field : clazz.getDeclaredFields()) {
            new FieldProcess(manger, field).process();
        }
    }
}
