package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.http.filter.Filter;
import xyz.thinktest.fastestapi.http.filter.FilterChain;
import xyz.thinktest.fastestapi.http.filter.HttpFilter;

import java.util.List;

/**
 * @author: aruba
 * @date: 2022-06-16
 */
public class HttpFilterHandler implements HttpFilter {
    @Override
    public void handleRequest(Requester requester, Responder responder, List<List<Filter>> filters) {
        FilterChainHandler filterChainHandler = new FilterChainHandler(filters);
        filterChainHandler.doFilter(requester, responder);
    }

    static class FilterChainHandler implements FilterChain {
        private final List<List<Filter>> filters;
        private int location;

        public FilterChainHandler(List<List<Filter>> filters){
            this.filters = filters;
            this.location = 0;
        }

        @Override
        public void doFilter(Requester requester, Responder responder) {
            if(this.location < this.filters.size()){
                List<Filter> filtersByLocation = this.filters.get(this.location++);
                if(filtersByLocation != null && filtersByLocation.size() > 0){
                    try {
                        filtersByLocation.forEach(filter -> filter.doFilter(requester, responder, this));
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
