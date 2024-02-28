package xyz.think.fastest.http;

import xyz.think.fastest.http.filter.Filter;
import xyz.think.fastest.http.filter.FilterConfig;
import xyz.think.fastest.http.ssl.SSLType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2020/10/24
 */
public class Settings {
    private boolean isShowRequestLog;
    private boolean isShowResponseLog;
    private boolean isCleanMetadata;
    private boolean isCleanBody;
    private SSLType sslType;
    private boolean followRedirects;
    private boolean followSslRedirects;
    private long connectTimeout;
    private long writeTimeout;
    private long readTimeout;
    private long callTimeout;
    private boolean retryOnConnectionFailure;
    private HttpClient client;
    private final List<Filter> filters;
    private boolean isSync;

    private Settings(){
        this.isShowRequestLog = true;
        this.isShowResponseLog = false;
        this.isCleanMetadata = false;
        this.isCleanBody = true;
        this.sslType = SSLType.DEFAULT;
        this.followRedirects = true;
        this.followSslRedirects = true;
        this.connectTimeout = 60L;
        this.writeTimeout = 60L;
        this.readTimeout = 60L;
        this.callTimeout = 120L;
        this.retryOnConnectionFailure = true;
        this.client = new HttpClient();
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

    public SSLType getSslType() {
        return sslType;
    }

    public Settings setSslType(SSLType sslType) {
        this.sslType = sslType;
        return this;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public Settings setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public boolean isFollowSslRedirects() {
        return followSslRedirects;
    }

    public Settings setFollowSslRedirects(boolean followSslRedirects) {
        this.followSslRedirects = followSslRedirects;
        return this;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public Settings setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public Settings setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public Settings setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public long getCallTimeout() {
        return callTimeout;
    }

    public Settings setCallTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
        return this;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public Settings setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
        return this;
    }

    public Settings setClient(HttpClient client) {
        this.client = client;
        return this;
    }

    public HttpClient getClient(){
        this.client.sslSocketFactory(sslType.getSslSocketFactory(),sslType.getTrustManager())
                .followRedirects(followRedirects)
                .followSslRedirects(followSslRedirects)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(retryOnConnectionFailure);
        return this.client;
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

    public static void copy(Settings src, Settings dst){
        dst.setCleanMetadata(src.isCleanMetadata())
                .setCleanBody(src.isCleanBody())
                .setSslType(src.getSslType())
                .setFollowRedirects(src.isFollowRedirects())
                .setFollowSslRedirects(src.isFollowSslRedirects())
                .setConnectTimeout(src.getConnectTimeout())
                .setWriteTimeout(src.getWriteTimeout())
                .setReadTimeout(src.getReadTimeout())
                .setCallTimeout(src.getCallTimeout())
                .setRetryOnConnectionFailure(src.isRetryOnConnectionFailure())
                .setClient(src.getClient())
                .setFilters(src.getFilters());
    }
}
