package xyz.thinktest.fastestapi.http.filter;

import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.http.Responder;

public interface Filter {

    void doFilter(Requester requester, Responder responder, FilterChain filterChain);
}
