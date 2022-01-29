package xyz.thinktest.fastestapi.core.internal.tool;

import org.reflections.Reflections;
import xyz.thinktest.fastestapi.core.enhance.ShutdownHook;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.core.internal.enhance.methodhelper.RestTempWrite;
import java.util.Set;

/**
 * @Date: 2021/10/31
 */
public final class ShutdownHookActuator {
    private ShutdownHookActuator(){}

    public static void writeApiTempJson(){
        if(RestTempWrite.getAllApi().isEmpty()){
            return;
        }
        Reflections reflections = ReflectionsUnit.INSTANCE.reflections;
        Set<Class<? extends ShutdownHook>> shutdownHooks = reflections.getSubTypesOf(ShutdownHook.class);
        Runtime runtime = Runtime.getRuntime();
        for(Class<? extends ShutdownHook> shutdownHook : shutdownHooks){
            runtime.addShutdownHook(EnhanceFactory.origin(shutdownHook));
        }
    }
}
