package xyz.thinktest.fastestapi.core.internal;

import xyz.thinktest.fastestapi.common.exceptions.InitializationException;
import xyz.thinktest.fastestapi.core.enhance.Initialize;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.core.internal.scanner.Reflections;
import xyz.thinktest.fastestapi.core.internal.scanner.ReflectionsUnit;
import xyz.thinktest.fastestapi.core.internal.scanner.ScannerUnit;
import xyz.thinktest.fastestapi.core.internal.tool.Banner;
import xyz.thinktest.fastestapi.core.internal.tool.poetry.Poetry;
import xyz.thinktest.fastestapi.http.DefaultRequester;
import xyz.thinktest.fastestapi.http.DefaultResponder;
import xyz.thinktest.fastestapi.http.internal.HttpCacheInternal;
import xyz.thinktest.fastestapi.utils.color.ColorPrint;
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
            throw new InitializationException("initialization fail", e);
        }
    }
    private static void runInit(){
        try {
            poetry();
        }catch (Throwable ignored){}
        DateUtil.SECOND.sleep(3);
    }

    private static void realInit(){
        httpInit();
        ScannerUnit.scan();
        runUserInit();
    }

    private static void httpInit(){
        try {
            String requesterClass = PropertyUtil.getProperty("fastest.api.http.requester");
            String responderClass = PropertyUtil.getProperty("fastest.api.http.responder");
            if(Objects.isNull(requesterClass)){
                requesterClass = DefaultRequester.class.getCanonicalName();
            }
            if (Objects.isNull(responderClass)) {
                responderClass = DefaultResponder.class.getCanonicalName();
            }
            httpCacheInternal.set("fastest.api.http.requester", Class.forName(requesterClass));
            httpCacheInternal.set("fastest.api.http.responder", Class.forName(responderClass));
        }catch (ClassNotFoundException e){
            throw new InitializationException("initialization http fail", e);
        }
    }

    private static void runUserInit(){
        Set<Class<? extends Initialize>> initializes = reflections.getSubTypesOf(Initialize.class);
        for(Class<? extends Initialize> clazz:initializes){
            Initialize initialize = EnhanceFactory.enhance(clazz);
            initialize.preHook();
        }
    }

    private static void poetry(){
        List<Class<? extends Poetry>> poetries = new ArrayList<>(reflections.getSubTypesOf(Poetry.class));
        Random random = new Random();
        Poetry poetry = EnhanceFactory.origin(poetries.get(random.nextInt(poetries.size())));
        String content = poetry.content();
        ColorPrint.CYAN.println(content);
    }
}
