package xyz.thinktest.fastestapi.core.enhance;

public interface Shutdownable {

    /**
     * 执行顺序
     */
    int order();

    /**
     * 收尾工作的hook方法，方法中完成收尾工作
     */
    void executor();
}
