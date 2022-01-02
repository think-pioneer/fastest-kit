package xyz.thinktest.fastest.core.internal.enhance;

import xyz.thinktest.fastest.logger.FastestLogger;
import xyz.thinktest.fastest.logger.FastestLoggerFactory;
import xyz.thinktest.fastest.utils.ObjectUtil;

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
        String key = ObjectUtil.format("{}-{}-{}", thisHash, nameHash, name);
        FastestLogger logger = cache.get(key);
        if(Objects.isNull(logger)) {
            logger = FastestLoggerFactory.getLogger(name);
            cache.put(key, logger);
            return logger;
        }
        return logger;
    }
}
