package xyz.think.fastest.http.filter;


import xyz.think.fastest.http.Responder;
import xyz.think.fastest.http.Requester;

/**
 * @author: aruba
 * @date: 2022-06-15
 */
public class Filter2 implements Filter {
    @Override
    public void doFilter(Requester requester, Responder responder, FilterChain filterChain) {
        System.out.println("before2");
        requester.metadata().setParameter("id", 3);
        filterChain.doFilter(requester, responder);
        System.out.println("after2");
        System.out.println(responder.bodyToString());
    }
}
