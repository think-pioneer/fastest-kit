package org.testng.step;

/**
 * @Date: 2022/1/2
 */
abstract class AbstractRecovery implements Recovery{

    @Override
    public boolean recovery(Step step) {
        return false;
    }

    @Override
    public boolean recovery(long timeOut, Step step) {
        return false;
    }

    @Override
    public boolean recovery(Step... steps) {
        return false;
    }

    @Override
    public boolean recovery(long timeOut, Step... steps) {
        return false;
    }
}
