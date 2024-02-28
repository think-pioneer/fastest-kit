package xyz.think.fastest.http.filter;

/**
 * @author: aruba
 * @date: 2022-06-16
 */
public class FilterConfig {
    private final int order;
    private final Filter filter;

    public FilterConfig(final int order, final Filter filter){
        this.order = order;
        this.filter = filter;
    }

    public FilterConfig(final Filter filter){
        this(-1, filter);
    }

    public int getOrder() {
        return order;
    }

    public Filter getFilter() {
        return filter;
    }
}
