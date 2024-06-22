package xyz.think.fastest.http.filter;

import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.Responder;

/**
 * http过滤器接口
 */
public interface Filter {

    /**
     * 执行过滤器
     * @param requester requester对象
     * @param responder responser对象
     * @param filterChain 过滤链。执行过滤器时，由过滤链触发，触发后将会执行所有过滤器。
     */
    void doFilter(Requester requester, Responder responder, FilterChain filterChain);
}
