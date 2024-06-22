package xyz.think.fastest.core.internal.shutdown;

import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.internal.enhance.methodhelper.RestTempCache;

/**
 * @author: aruba
 *
 * @date: 2022-01-28
 */
@Component
public class RestWrite implements ShutdownInternal {

    @Override
    public int order() {
        return ShutdownOrderInternalManager.getInstance().consumer(RestWrite.class);
    }

    @Override
    public void executor() {
        if(RestTempCache.isEmpty()){
            return;
        }
        RestTempCache.save();
    }
}
