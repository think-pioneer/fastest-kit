package xyz.think.fastest.http;

import xyz.think.fastest.core.ApplicationBean;
import xyz.think.fastest.http.filter.HttpFilter;
import xyz.think.fastest.http.internal.HttpCacheInternal;

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
     * 等价于metadata().copy(metadata);
     */
    Requester metadata(Metadata metadata);

    /**
     *获取客户端的设置项
     */
    Settings settings();


    /**
     * 设置客户端的设置项
     * 等价于settings().copy(settings)
     */
    Requester settings(Settings settings);
    /**
     * 获取客户端
     */
    HttpClient httpClient();

    /**
     * 设置客户端
     * 等价于httpClient().copy(httpClient)
     */
    Requester httpClient(HttpClient httpClient);

    /**
     * 设置同步请求
     */
    Requester sync();

    /**
     * 设置异步请求
     */
    Requester async();

    /**
     * 对响应执行断言操作
     */
    Asserts asserts();

    /**
     * 请求完成后，设置响应对象，内部使用
     */
    void setResponder(Responder responder);

    /**
     * 获取responder
     */
    Responder getResponder();

    /**
     * 发起请求
     */
    default Requester send(){
        HttpFilter httpFilter = new HttpFilterHandler();
        Class<Responder> responderType = HttpCacheInternal.INSTANCE.get("fastest.http.responder");
        Responder responder = ApplicationBean.getEnhanceBean(responderType);
        httpFilter.handleRequest(this, responder,this.settings().getFilters());
        return this;
    }
}
