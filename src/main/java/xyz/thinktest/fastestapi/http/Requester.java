package xyz.thinktest.fastestapi.http;

/**
 * http client
 */
public interface Requester {

    /**
     * 设置http元数据
     */
    Metadata metadata();

    /**
     * 设置http元数据
     */
    Metadata metadata(Metadata metadata);

    /**
     *设置客户端的设置项
     */
    Settings settings();

    /**
     * 获取responder
     */
    Responder getResponder();

    /**
     * 打印http响应的关键信息
     */
    void printResponse();

    /**
     * 执行同步请求
     */
    void sync();

    /**
     * 执行异步请求
     */
    void async();

    /**
     * 对响应执行断言操作
     */
    Asserts asserts();
}
