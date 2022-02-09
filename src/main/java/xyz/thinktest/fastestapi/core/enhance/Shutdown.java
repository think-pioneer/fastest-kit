package xyz.thinktest.fastestapi.core.enhance;

/**
 * @author: aruba
 * @date: 2022-01-28
 * 提供给用户自定义收尾的接口，会在测试结束推出系统时执行用户自定义收尾工作
 */
public interface Shutdown {

    /**
     * 收尾工作的hook方法，方法中完成收尾工作
     */
    void postHook();
}
