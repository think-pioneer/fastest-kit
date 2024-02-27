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
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;

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
            CompletableFuture<Void> loading = CompletableFuture.runAsync(InitializationActuator::loading);
            CompletableFuture<Void> working = CompletableFuture.runAsync(InitializationActuator::working);
            CompletableFuture.allOf(loading, working).get();
            ColorPrint.GREEN.println("Initialize complete!!!");
        }catch (Throwable e){
            throw new InitializationException("Initialize fail", e);
        }
    }

    /**
     * 执行初始化操作时，会打印一些诗词
     */
    private static void loading(){
        // 如果读取诗词失败，则直接sleep3秒
        try {
            boolean disable = PropertyUtil.getOrDefault("fasttest.poetry.disable", false);
            if (disable) {
                return;
            }
            poetry();
        }catch (Throwable ignored){
            DateUtil.SECOND.sleep(3);
        }
    }

    /**
     * 执行初始化操作
     */
    private static void working(){
        systemInit();
        userInit();
    }

    /**
     * 执行框架自身的初始化操作
     * 会在用户初始化操作之前执行
     * 用户初始化{@link #userInit()}
     */
    private static void systemInit() {
        Set<Class<? extends InitializeInternal>> initializeTypes = reflections.getSubTypesOf(InitializeInternal.class);
        initializeTypes.stream().map(EnhanceFactory::enhance).sorted(Comparator.comparing(Initializable::order)).forEach(Initializable::executor);
    }

    /**
     * 执行用户的初始化操作
     * 会在框架初始化完成之后执行
     * 框架初始化{@link #systemInit()}
     */
    private static void userInit(){
        Set<Class<? extends Initialize>> initializeTypes = reflections.getSubTypesOf(Initialize.class);
        initializeTypes.stream().map(EnhanceFactory::enhance).sorted(Comparator.comparing(Initialize::order)).forEach(Initialize::executor);
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
