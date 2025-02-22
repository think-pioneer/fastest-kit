package xyz.think.fastest.core.internal.enhance;

import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.logger.FastestLoggerFactory;
import xyz.think.fastest.utils.string.StringUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Date: 2021/11/7
 */
public class LogFactory {
    private static final int thisHash = LogFactory.class.hashCode();
    private static final HashMap<String, FastestLogger> cache = new HashMap<>();

    public static FastestLogger getLogger(String name){
        int nameHash = name.hashCode();
        String key = StringUtils.format("{0}-{1}-{2}", thisHash, nameHash, name);
        FastestLogger logger = cache.get(key);
        if(Objects.isNull(logger)) {
            logger = FastestLoggerFactory.getLogger(name);
            cache.put(key, logger);
            return logger;
        }
        return logger;
    }
}
