package org.testng.step;

/**
 * @Date: 2020/12/19
 */
public interface RecoveryExecutor {

    /**
     * 恢复时可能需要多个step
     * 需要注意，recovery操作会在一个子线程中进行
     * @param timeOut 超时时间
     * @param forceStop 超时后是否强制停止
     * @param steps recovery步骤
     */
    boolean execute(long timeOut, boolean forceStop, Step... steps);
}
