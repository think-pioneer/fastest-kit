package xyz.thinktest.fastestapi.http;

/**
 * http client
 */
public interface Requester {

    /**
     * 设置或获取http元数据
     */
    Metadata metadata();

    /**
     * 设置http元数据
     */
    Metadata metadata(Metadata metadata);

    /**
     *获取客户端的设置项
     */
    Settings settings();

    /**
     * 获取responder
     */
    Responder getResponder();

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
