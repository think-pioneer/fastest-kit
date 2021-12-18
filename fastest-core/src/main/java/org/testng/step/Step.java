package org.testng.step;

/**
 * @Date: 2021/10/30
 */
public interface Step {
    /**
     * recovery the test environment data when the use cas is executed
     * @return recovery result
     */
    boolean recovery();
}
