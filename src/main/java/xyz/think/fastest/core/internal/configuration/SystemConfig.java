package xyz.think.fastest.core.internal.configuration;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class SystemConfig {

    public static void config(){
        disableWarning();
        disableSlf4jBindingWarning();
    }

    /**
     * 禁止slf4j的warning信息出现
     * 多个slf4j的实现类存在时会有告警信息。
     */
    private static void disableWarning(){
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        }catch (Exception ignored){

        }
    }

    /**
     * 禁止Android平台上的slf4j的告警
     * @see #disableWarning()
     */
    private static void disableSlf4jBindingWarning(){
        System.setProperty("java.vendor.url", "android");
    }
}
