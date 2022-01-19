package xyz.thinktest.fastestapi.core.internal.configuration;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class SystemConfig {

    public static void disableWarning(){
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
}
