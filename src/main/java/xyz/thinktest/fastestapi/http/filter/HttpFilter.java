package xyz.thinktest.fastestapi.http.filter;

import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.http.Responder;

import java.util.List;

/**
 * @author: aruba
 * @date: 2022-06-16
 */
public interface HttpFilter {
    void handleRequest(Requester requester, Responder responder, List<Filter> filters);
}
