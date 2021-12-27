package xyz.thinktest.fastest.core.internal;

import xyz.thinktest.fastest.common.exceptions.FastestBasicException;

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
    }

}
