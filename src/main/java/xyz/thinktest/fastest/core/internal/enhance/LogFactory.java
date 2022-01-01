package xyz.thinktest.fastest.core.internal.enhance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Date: 2021/11/7
 */
public class LogFactory {
    private static final int thisHash = LogFactory.class.hashCode();
    private static final HashMap<String, Logger> cache = new HashMap<>();

    public static Logger getLogger(String name){
        int nameHash = name.hashCode();
        String key = ObjectUtil.format("{}-{}-{}", thisHash, nameHash, name);
        Logger logger = cache.get(key);
        if(Objects.isNull(logger)) {
            logger = LogManager.getLogger(name);
            cache.put(key, logger);
            return logger;
        }
        return logger;
    }
}
