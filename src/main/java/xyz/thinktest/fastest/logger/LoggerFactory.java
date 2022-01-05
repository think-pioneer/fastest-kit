package xyz.thinktest.fastest.logger;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2022/1/2
 */
enum LoggerFactory {
    FACTORY{
        @Override
        public FastestLogger getLogger(String name) {
            FastestLogger logger = this.loggerCache.get(name);
            if(Objects.isNull(logger)){
                logger = new FastestLoggerImplement();
                this.loggerCache.putIfAbsent(name, logger);
            }
            return logger;
        }

        @Override
        public FastestLogger getLogger(Class<?> clazz) {
            return getLogger(clazz.getName());
        }
    };


    final ConcurrentHashMap<String, FastestLogger> loggerCache;

    LoggerFactory(){
        loggerCache = new ConcurrentHashMap<>();
    }

    public abstract FastestLogger getLogger(String name);

    public abstract FastestLogger getLogger(Class<?> clazz);
}
