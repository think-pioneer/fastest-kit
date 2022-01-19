package xyz.thinktest.fastestapi.logger;

/**
 * @Date: 2022/1/2
 */
public final class FastestLoggerFactory {

    public static synchronized FastestLogger getLogger(String name){
        return LoggerFactory.FACTORY.getLogger(name);
    }

    public static synchronized FastestLogger getLogger(Class<?> clazz){
        return LoggerFactory.FACTORY.getLogger(clazz);
    }
}
