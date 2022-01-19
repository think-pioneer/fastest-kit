package xyz.thinktest.fastestapi.core.internal;

import xyz.thinktest.fastestapi.core.internal.configuration.SystemConfig;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;

/**
 * @Date: 2021/12/19
 */
public class Initialization {
    public static void init(){
        try{
            realInit();
        }catch (Exception e){
            throw new FastestBasicException("initialization fail", e);
        }
    }

    private static void realInit(){
        SystemConfig.disableWarning();
    }

}
