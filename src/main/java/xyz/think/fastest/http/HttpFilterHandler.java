package xyz.think.fastest.http;

import xyz.think.fastest.http.filter.Filter;
import xyz.think.fastest.http.filter.FilterChain;
import xyz.think.fastest.http.filter.HttpFilter;

import java.util.List;

/**
 * @author: aruba
 * @date: 2022-06-16
 */
public final class HttpFilterHandler implements HttpFilter {
    @Override
    public void handleRequest(Requester requester, Responder responder, List<Filter> filters) {
        FilterChainHandler filterChainHandler = new FilterChainHandler(filters);
        filterChainHandler.doFilter(requester, responder);
    }

    static class FilterChainHandler implements FilterChain {
        private final List<Filter> filters;
        private int location;

        public FilterChainHandler(List<Filter> filters){
            this.filters = filters;
            this.location = 0;
        }

        @Override
        public void doFilter(Requester requester, Responder responder) {
            if(this.location < this.filters.size()){
                Filter filterByLocation = this.filters.get(this.location++);
                if(filterByLocation != null){
                    try {
                        filterByLocation.doFilter(requester, responder, this);
                    }finally {
                        this.location--;
                    }
                }
            }else{
                Sender sender = new Sender(requester.metadata(), requester.settings().getClient().getClient());
                if(requester.settings().isSync()){
                    sender.sync();
                }else{
                    sender.async();
                }
                responder.init(sender.getResponse());
                requester.setResponder(responder);
            }
        }
    }
}
