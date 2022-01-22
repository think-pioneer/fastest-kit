package xyz.thinktest.fastestapi.core.internal;

import xyz.thinktest.fastestapi.core.internal.configuration.SystemConfig;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.http.DefaultResponder;
import xyz.thinktest.fastestapi.http.HttpCacheInternal;
import xyz.thinktest.fastestapi.utils.files.YamlUtil;

import java.util.Objects;

/**
 * @Date: 2021/12/19
 */
public class Initialization {
    private static HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;

    public static void init(){
        try{
            realInit();
            readResponder();
        }catch (Exception e){
            throw new FastestBasicException("initialization fail", e);
        }
    }

    private static void realInit(){
        SystemConfig.disableWarning();
    }

    private static void readResponder() throws ClassNotFoundException {
        String responderClass = YamlUtil.getString("fastest.api.http.responder");
        if(Objects.isNull(responderClass)){
            responderClass = DefaultResponder.class.getCanonicalName();
        }
        httpCacheInternal.set("fastest.api.http.responder", Class.forName(responderClass));
    }

}
