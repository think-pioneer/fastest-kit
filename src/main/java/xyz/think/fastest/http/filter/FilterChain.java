package xyz.think.fastest.http.filter;

import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.Responder;

/**
 * @author: aruba
 * @date: 2022-06-15
 */
public interface FilterChain {

    void doFilter(Requester requester, Responder responder);
}
