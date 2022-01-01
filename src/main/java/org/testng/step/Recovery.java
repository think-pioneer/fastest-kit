package org.testng.step;

/**
 * @Date: 2020/12/19
 */
public interface Recovery {

    /**
     *Restore the test environment or data and provide the user-defined restorer
     * @return
     */
    boolean recovery(Step step);

    /**
     *Restore the test environment or data and provide the user-defined restorer
     * @return
     */
    boolean recovery(long timeOut, Step step);

    /**
     * 恢复时可能需要多个step
     */
    boolean recovery(Step... steps);

    /**
     * 恢复时可能需要多个step
     */
    boolean recovery(long timeOut, Step... steps);
}
