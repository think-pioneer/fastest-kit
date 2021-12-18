package org.fastest.core.internal.enhance;

import org.fastest.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Date: 2021/11/7
 */
public class LogFactory {
    private static final int thisHash = LogFactory.class.hashCode();
    private static final HashMap<String, Object> cache = new HashMap<>();

    public static Logger getLogger(String name){
        int nameHash = name.hashCode();
        String key = ObjectUtil.format("{}-{}-{}", thisHash, nameHash, name);
        Logger logger = (Logger) cache.get(key);
        if(Objects.isNull(logger)) {
            logger = LoggerFactory.getLogger(name);
            cache.put(key, logger);
            return logger;
        }
        return logger;
    }
}
