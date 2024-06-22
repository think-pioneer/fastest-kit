package org.testng.step;

/**
 * @Date: 2022/1/2
 */
abstract class AbstractRecoveryExecutor implements RecoveryExecutor {

    @Override
    public boolean execute(long timeOut, boolean forceStop, Step... steps) {
        return false;
    }
}
