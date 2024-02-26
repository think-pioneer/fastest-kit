package xyz.thinktest.fastestapi.core.internal.initialization;

import xyz.thinktest.fastestapi.common.exceptions.InitializationException;
import xyz.thinktest.fastestapi.core.enhance.Initializable;
import xyz.thinktest.fastestapi.core.enhance.Initialize;
import xyz.thinktest.fastestapi.core.internal.enhance.EnhanceFactory;
import xyz.thinktest.fastestapi.core.internal.scanner.Reflections;
import xyz.thinktest.fastestapi.core.internal.scanner.ReflectionsUnit;
import xyz.thinktest.fastestapi.core.internal.tool.Banner;
import xyz.thinktest.fastestapi.core.internal.tool.poetry.Poetry;
import xyz.thinktest.fastestapi.utils.color.ColorPrint;
import xyz.thinktest.fastestapi.utils.dates.DateUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @Date: 2021/12/19
 */
public class InitializationActuator {
    private static final Reflections reflections = ReflectionsUnit.INSTANCE.reflections;
    private static final Banner banner = Banner.INSTANCE;

    public static void init(){
        try{
            banner.print();
            ColorPrint.GREEN.println("Initializing, please wait...");
            CompletableFuture<Void> coffee = CompletableFuture.runAsync(InitializationActuator::runInit);
            CompletableFuture<Void> runInit = CompletableFuture.runAsync(InitializationActuator::realInit);
            CompletableFuture.allOf(coffee, runInit).get();
            ColorPrint.GREEN.println("InitializationActuator complete!!!");
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

    private static void systemInit() {
        Set<Class<? extends InitializeInternal>> initializeTypes = reflections.getSubTypesOf(InitializeInternal.class);
        initializeTypes.stream().map(EnhanceFactory::enhance).sorted(Comparator.comparing(Initializable::order)).forEach(Initializable::executor);
    }
    private static void userInit(){
        Set<Class<? extends Initialize>> initializeTypes = reflections.getSubTypesOf(Initialize.class);
        initializeTypes.stream().map(EnhanceFactory::enhance).sorted(Comparator.comparing(Initialize::order)).forEach(Initialize::executor);
    }


    private static void realInit(){
        systemInit();
        userInit();
    }

    /**
     * 打印诗词
     */
    private static void poetry(){
        List<Class<? extends Poetry>> poetries = new ArrayList<>(reflections.getSubTypesOf(Poetry.class));
        Random random = new Random();
        Poetry poetry = EnhanceFactory.origin(poetries.get(random.nextInt(poetries.size())));
        String content = poetry.content();
        ColorPrint.CYAN.println(content);
    }
}
