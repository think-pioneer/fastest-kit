package xyz.think.fastest.core.enhance;

public interface Initializable {

    /**
     * 执行顺序
     */
    int order();

    /**
     * 初始化操作的hook方法，初始化操作需要在该方法中完成
     */
    void executor();
}
