package xyz.think.fastest.http.filter;

import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.Responder;

/**
 * 过滤链。负责将所有过滤器连接起来，并负责启动执行过滤器，当一个过滤器开始执行后，将会执行完所有的过滤器才会终止。
 * @author: aruba
 * @date: 2022-06-15
 */
public interface FilterChain {

    /**
     * 负责执行整个过滤链
     * @param requester http request 对象
     * @param responder http response 对象
     */
    void doFilter(Requester requester, Responder responder);
}
