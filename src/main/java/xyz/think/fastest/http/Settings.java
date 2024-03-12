package xyz.think.fastest.http;

import xyz.think.fastest.http.filter.Filter;
import xyz.think.fastest.http.filter.FilterConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/10/24
 */
public class Settings {
    private boolean isShowRequestLog;
    private boolean isShowResponseLog;
    private boolean isCleanMetadata;
    private boolean isCleanBody;
    private final List<Filter> filters;
    private boolean isSync;

    private Settings(){
        this.isShowRequestLog = true;
        this.isShowResponseLog = false;
        this.isCleanMetadata = false;
        this.isCleanBody = true;
        this.filters = new ArrayList<>();
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

    Settings setFilters(List<Filter> filters){
        this.filters.addAll(filters);
        return this;
    }

    public Settings setFilter(FilterConfig filterConfig){
        int index = Math.max(Math.min(filterConfig.getOrder(), this.filters.size()), 0);
        this.filters.add(index, filterConfig.getFilter());
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
        return this.filters;
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
