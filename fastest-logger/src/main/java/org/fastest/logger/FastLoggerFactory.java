package org.fastest.logger;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Date: 2021/12/18
 */
public class FastLoggerFactory {
    private static final ConcurrentMap<String, FastLogger> loggerMap = new ConcurrentHashMap<>();

    public static FastLogger getLogger(String name){
        FastLogger logger = loggerMap.get(name);
        if(Objects.nonNull(logger)){
            return logger;
        }
        logger = FastLogger.getLogger(name);
        loggerMap.putIfAbsent(name, logger);
        return logger;
    }

    public static FastLogger getLogger(Class<?> clazz){
        return getLogger(clazz.getName());
    }
}
