package xyz.thinktest.fastestapi.core.internal.shutdown;

import org.reflections.Reflections;
import xyz.thinktest.fastestapi.core.enhance.Shutdown;
import xyz.thinktest.fastestapi.core.enhance.Shutdownable;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.core.internal.scanner.ReflectionsUnit;

import java.util.Comparator;
import java.util.Set;

/**
 * 拦截程序结束的操作，并处理一些额外的事务，之后在进行真正的shutdown
 * @Date: 2021/10/31
 */
public final class ShutdownActuator {
    private ShutdownActuator(){}

    public static void execute(){
        Reflections reflections = ReflectionsUnit.INSTANCE.reflections;
        Runtime runtime = Runtime.getRuntime();
        Thread thread = new Thread(() -> {
            executor(reflections, Shutdown.class, Comparator.comparing(Shutdown::order));  // 先关闭用户自定义的shutdown
            executor(reflections, ShutdownInternal.class, Comparator.comparing(ShutdownInternal::order)); //再关闭系统的shutdown
        });
        runtime.addShutdownHook(thread);
    }

    private static <T extends Shutdownable> void executor(Reflections reflections, Class<T> type, Comparator<? super T> comparator) {
        Set<Class<? extends T>> shutdownHookTypes = reflections.getSubTypesOf(type);
        shutdownHookTypes.stream().map(EnhanceFactory::origin).sorted(comparator).forEach(Shutdownable::executor);
    }
}
