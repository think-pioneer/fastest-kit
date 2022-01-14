package xyz.thinktest.fastest.utils;

public class CompletableUtil {

    public static int assignProcessMax(int size){
        return Math.max(size/Runtime.getRuntime().availableProcessors(), 1);
    }
}
