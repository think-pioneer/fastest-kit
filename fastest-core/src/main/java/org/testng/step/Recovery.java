package org.testng.step;

/**
 * @Date: 2021/10/30
 */
@FunctionalInterface
interface Recovery {

    /**
     * recovery the test environment data when the test case is executed
     * @return recovery result
     */
    boolean recovery();
}
