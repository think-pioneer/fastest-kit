package xyz.thinktest.fastestapi.core.internal.tool;

import org.reflections.Reflections;
import xyz.thinktest.fastestapi.core.enhance.Shutdown;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.core.internal.enhance.methodhelper.RestTempWrite;
import xyz.thinktest.fastestapi.core.internal.scanner.ReflectionsUnit;

import java.util.Set;

/**
 * 拦截程序结束的操作，并处理一些额外的事务，之后在进行真正的shutdown
 * @Date: 2021/10/31
 */
public final class ShutdownHookActuator {
    private ShutdownHookActuator(){}

    public static void execute(){
        if(RestTempWrite.getAllApi().isEmpty()){
            return;
        }
        Reflections reflections = ReflectionsUnit.INSTANCE.reflections;
        Set<Class<? extends Shutdown>> shutdownHooks = reflections.getSubTypesOf(Shutdown.class);
        Runtime runtime = Runtime.getRuntime();
        for(Class<? extends Shutdown> shutdownHookType : shutdownHooks){
            Thread thread = new Thread(() -> {
                Shutdown shutdown = EnhanceFactory.origin(shutdownHookType);
                shutdown.postHook();
            });
            runtime.addShutdownHook(thread);
        }
    }
}
