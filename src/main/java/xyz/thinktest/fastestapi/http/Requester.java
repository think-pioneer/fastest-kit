package xyz.thinktest.fastestapi.http;

import okhttp3.Response;
import xyz.thinktest.fastestapi.core.ApplicationBean;
import xyz.thinktest.fastestapi.http.filter.HttpFilter;
import xyz.thinktest.fastestapi.http.internal.HttpCacheInternal;

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
    Requester metadata(Metadata metadata);

    /**
     *获取客户端的设置项
     */
    Settings settings();


    /**
     * 获取客户端的设置项
     */
    Requester settings(Settings settings);

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
        Class<Responder> responderType = HttpCacheInternal.INSTANCE.get("fastest.api.http.responder");
        Responder responder = ApplicationBean.getEnhanceBean(responderType);
        httpFilter.handleRequest(this, responder,this.settings().getFilters());
        return this;
    }
}
