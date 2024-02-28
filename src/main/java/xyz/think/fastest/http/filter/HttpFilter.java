package xyz.think.fastest.http.filter;

import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.Responder;

import java.util.List;

/**
 * @author: aruba
 * @date: 2022-06-16
 */
public interface HttpFilter {
    void handleRequest(Requester requester, Responder responder, List<Filter> filters);
}
