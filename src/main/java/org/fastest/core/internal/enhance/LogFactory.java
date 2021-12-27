package org.fastest.core.internal.enhance;

import org.fastest.logger.FastLogger;
import org.fastest.logger.FastLoggerFactory;
import org.fastest.utils.ObjectUtil;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Date: 2021/11/7
 */
public class LogFactory {
    private static final int thisHash = LogFactory.class.hashCode();
    private static final HashMap<String, FastLogger> cache = new HashMap<>();

    public static FastLogger getLogger(String name){
        int nameHash = name.hashCode();
        String key = ObjectUtil.format("{}-{}-{}", thisHash, nameHash, name);
        FastLogger logger = cache.get(key);
        if(Objects.isNull(logger)) {
            logger = FastLoggerFactory.getLogger(name);
            cache.put(key, logger);
            return logger;
        }
        return logger;
    }
}
