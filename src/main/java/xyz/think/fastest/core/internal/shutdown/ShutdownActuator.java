package xyz.think.fastest.core.internal.shutdown;

import org.reflections.Reflections;
import xyz.think.fastest.core.enhance.Shutdown;
import xyz.think.fastest.core.enhance.Shutdownable;
import xyz.think.fastest.core.internal.enhance.EnhanceFactory;
import xyz.think.fastest.core.internal.scanner.ReflectionsUnit;
import xyz.think.fastest.core.internal.tool.AnnotationTool;

import java.util.Comparator;
import java.util.Set;

/**
 * 拦截程序结束的操作，并处理一些额外的事务，之后在进行真正的shutdown
 * @Date: 2021/10/31
 */
public final class ShutdownActuator {
    private ShutdownActuator(){}

    public static void register(){
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
        shutdownHookTypes.stream().filter(AnnotationTool::hasComponentAnnotation).map(EnhanceFactory::origin).sorted(comparator).forEach(Shutdownable::executor);
    }

}
