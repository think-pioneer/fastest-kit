package xyz.think.fastest.http.filter;

import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.Responder;

public interface Filter {

    void doFilter(Requester requester, Responder responder, FilterChain filterChain);
}
