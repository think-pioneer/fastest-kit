package xyz.think.fastest.http.filter;


import xyz.think.fastest.http.Responder;
import xyz.think.fastest.http.Requester;

/**
 * @author: aruba
 * @date: 2022-06-15
 */
public class Filter1 implements Filter {
    @Override
    public void doFilter(Requester requester, Responder responder, FilterChain filterChain) {
        System.out.println("before1");
        filterChain.doFilter(requester, responder);
        System.out.println("after1");
    }
}
