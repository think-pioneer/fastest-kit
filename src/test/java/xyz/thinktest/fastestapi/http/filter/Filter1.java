package xyz.thinktest.fastestapi.http.filter;


import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.http.Responder;

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
