package xyz.thinktest.fastestapi.http.filter;

import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.http.Responder;

/**
 * @author: aruba
 * @date: 2022-06-15
 */
public interface FilterChain {

    void doFilter(Requester requester, Responder responder);
}
