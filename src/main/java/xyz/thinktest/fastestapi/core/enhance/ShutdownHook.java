package xyz.thinktest.fastestapi.core.enhance;

/**
 * @author: aruba
 * @date: 2022-01-28
 */
public abstract class ShutdownHook extends Thread {

    public abstract void run();
}
