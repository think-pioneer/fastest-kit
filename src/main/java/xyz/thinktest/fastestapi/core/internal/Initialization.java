package xyz.thinktest.fastestapi.core.internal;

import xyz.thinktest.fastestapi.core.enhance.Initialize;
import xyz.thinktest.fastestapi.core.internal.configuration.SystemConfig;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.core.internal.scanner.Reflections;
import xyz.thinktest.fastestapi.core.internal.scanner.ReflectionsUnit;
import xyz.thinktest.fastestapi.core.internal.scanner.ScannerUnit;
import xyz.thinktest.fastestapi.core.internal.tool.Banner;
import xyz.thinktest.fastestapi.core.internal.tool.poetry.Poetry;
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
    private static final Reflections reflections = ReflectionsUnit.INSTANCE.reflections;
    private static final Banner banner = Banner.INSTANCE;

    public static void init(){
        try{
            banner.print();
            ColorPrint.GREEN.println("Project are initializing, please wait...");
            CompletableFuture<Void> coffee = CompletableFuture.runAsync(Initialization::runInit);
            CompletableFuture<Void> runInit = CompletableFuture.runAsync(Initialization::realInit);
            CompletableFuture.allOf(coffee, runInit).get();
            ColorPrint.GREEN.println("Initialization complete!!!");
        }catch (Throwable e){
            throw new FastestBasicException("initialization fail", e);
        }
    }
    private static void runInit(){
        poetry();
        DateUtil.SECOND.sleep(3);
    }

    private static void realInit(){
        SystemConfig.disableWarning();
        SystemConfig.disableSlf4jBindingWarning();
        readResponder();
        ScannerUnit.scan();
        runUserInit();
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

    private static void runUserInit(){
        Set<Class<? extends Initialize>> initializes = reflections.getSubTypesOf(Initialize.class);
        for(Class<? extends Initialize> clazz:initializes){
            Initialize initialize = EnhanceFactory.enhance(clazz);
            initialize.pre();
        }
    }

    private static void poetry(){
        Set<Class<? extends Poetry>> poetries = reflections.getSubTypesOf(Poetry.class);
        for(Class<? extends Poetry> clazz:poetries){
            Poetry poetry = EnhanceFactory.enhance(clazz);
            poetry.show();
        }
    }
}
