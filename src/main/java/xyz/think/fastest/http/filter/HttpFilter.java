package xyz.think.fastest.http.filter;

import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.Responder;

import java.util.List;

/**
 * 处理http filter，使之能够通过FilterChain执行Filter
 * @author: aruba
 * @date: 2022-06-16
 */
public interface HttpFilter {
    void handleRequest(Requester requester, Responder responder, List<Filter> filters);
}
