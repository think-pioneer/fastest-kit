package xyz.think.fastest.http;

import org.checkerframework.checker.units.qual.C;
import xyz.think.fastest.http.filter.Filter;
import xyz.think.fastest.http.filter.FilterConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Date: 2020/10/24
 */
public class Settings {
    /**
     * 是否展示请求log，默认false。
     */
    private boolean isShowRequestLog;
    /**
     * 是否展示响应log，默认false。
     */
    private boolean isShowResponseLog;
    /**
     * 是否清空元信息，默认false。
     */
    private boolean isCleanMetadata;
    /**
     * 是否清空body，默认true。
     */
    private boolean isCleanBody;
    /**
     * 过滤器缓存
     */
    private final Map<Integer, List<FilterConfig>> filterMap;
    /**
     * http是否使用同步请求，默认true。
     */
    private boolean isSync;

    private Settings(){
        this.isShowRequestLog = true;
        this.isShowResponseLog = false;
        this.isCleanMetadata = false;
        this.isCleanBody = true;
        this.filterMap = new ConcurrentHashMap<>();
        this.isSync = true;
    }

    public boolean isShowRequestLog(){
        return this.isShowRequestLog;
    }

    public Settings setIsShowRequestLog(boolean isShowRequestLog){
        this.isShowRequestLog = isShowRequestLog;
        return this;
    }

    public boolean isShowResponseLog(){
        return this.isShowResponseLog;
    }

    public Settings setIsShowResponseLog(boolean isShowResponseLog){
        this.isShowResponseLog = isShowResponseLog;
        return this;
    }

    public boolean isCleanMetadata() {
        return isCleanMetadata;
    }

    public Settings setCleanMetadata(boolean cleanMetadata) {
        isCleanMetadata = cleanMetadata;
        return this;
    }

    public boolean isCleanBody() {
        return isCleanBody;
    }

    public Settings setCleanBody(boolean cleanBody) {
        isCleanBody = cleanBody;
        return this;
    }

    public Settings setFilters(List<Filter> filters){
        if (filters == null || filters.size() == 0){
            return this;
        }
        int start = this.filterMap.size();
        for(int i = 0; i < filters.size(); i++){
            int order = start+i;
            List<FilterConfig> filterConfigs = this.filterMap.get(order);
            if (filterConfigs == null){
                filterConfigs = new CopyOnWriteArrayList<>();
                this.filterMap.put(order, filterConfigs);
            }
            filterConfigs.add(new FilterConfig(order, filters.get(i)));
        }
        return this;
    }

    public Settings setFilters(Filter... filters){
        this.setFilters(new CopyOnWriteArrayList<>(filters));
        return this;
    }

    public Settings setFilterConfigs(List<FilterConfig> filterConfigs){
        if (filterConfigs == null || filterConfigs.size() == 0){
            return this;
        }
        for(FilterConfig filterConfig:filterConfigs){
            List<FilterConfig> filterConfigList = this.filterMap.get(filterConfig.getOrder());
            if(filterConfigList == null) {
                filterConfigList = new CopyOnWriteArrayList<>();
                this.filterMap.put(filterConfig.getOrder(), filterConfigList);
            }
            filterConfigList.add(filterConfig);
        }
        return this;
    }

    public Settings setFilterConfigs(FilterConfig... filterConfigs){
        this.setFilterConfigs(new CopyOnWriteArrayList<>(filterConfigs));
        return this;
    }

    public Settings setFilter(Filter filter){
        int order = this.filterMap.size();
        this.filterMap.put(order, new CopyOnWriteArrayList<FilterConfig>(){{add(new FilterConfig(order, filter));}});
        return this;
    }

    public Settings setFilter(int order, Filter filter){
        this.setFilterConfigs(new FilterConfig(order, filter));
        return this;
    }

    public boolean isSync(){
        return this.isSync;
    }

    public Settings setIsSync(boolean isSync){
        this.isSync = isSync;
        return this;
    }

    List<Filter> getFilters(){
        return this.filterMap.values().stream().flatMap(Collection::stream).map(FilterConfig::getFilter).collect(Collectors.toList());
    }

    List<FilterConfig> getFilterConfigs(){
        return this.filterMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static Settings create(){
        return new Settings();
    }

    public static Settings create(Settings settings){
        return settings == null ? new Settings() : settings;
    }

    public void copy(Settings src){
        this.setIsShowRequestLog(src.isShowRequestLog())
                .setIsShowResponseLog(src.isShowResponseLog())
                .setCleanMetadata(src.isCleanMetadata())
                .setCleanBody(src.isCleanBody())
                .setIsSync(src.isSync())
                .setFilters(src.getFilters());
    }
}
