package xyz.thinktest.fastestapi.core.internal;

import xyz.thinktest.fastestapi.core.internal.configuration.SystemConfig;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.core.internal.tool.Banner;
import xyz.thinktest.fastestapi.core.internal.tool.Scanner;
import xyz.thinktest.fastestapi.http.DefaultResponder;
import xyz.thinktest.fastestapi.http.HttpCacheInternal;
import xyz.thinktest.fastestapi.utils.ColorPrint;
import xyz.thinktest.fastestapi.utils.dates.DateUtil;
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @Date: 2021/12/19
 */
public class Initialization {
    private static final HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;
    private static final Banner banner = Banner.INSTANCE;

    public static void init(){
        try{
            banner.print();
            ColorPrint.GREEN.println("Resource are initializing, please wait...");
            CompletableFuture<Void> banner = CompletableFuture.runAsync(Initialization::runInit);
            CompletableFuture<Void> runInit = CompletableFuture.runAsync(Initialization::realInit);
            CompletableFuture.allOf(banner, runInit).get();
        }catch (Throwable e){
            throw new FastestBasicException("initialization fail", e);
        }
    }
    private static void runInit(){
        DateUtil.SECOND.sleep(3);
    }

    private static void realInit(){
        SystemConfig.disableWarning();
        SystemConfig.disableSlf4jBindingWarning();
        readResponder();
        Scanner.scan();
    }

    private static void readResponder() {
        try {
            String responderClass = PropertyUtil.getProperty("fastest.api.http.responder");
            if (Objects.isNull(responderClass)) {
                responderClass = DefaultResponder.class.getCanonicalName();
            }
            httpCacheInternal.set("fastest.api.http.responder", Class.forName(responderClass));
        }catch (ClassNotFoundException e){
            throw new FastestBasicException("load class fail", e);
        }
    }
}
