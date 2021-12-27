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
    boolean recovery(Step step, long timeOut);
}
