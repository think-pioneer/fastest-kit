package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.http.ssl.SSLType;
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;

/**
 * @Date: 2020/10/24
 */
public class Settings {
    private boolean isCleanMetadata;
    private boolean isCleanBody;
    private Boolean showRequestLog;
    private Boolean showResponseLog;
    private SSLType sslType;
    private boolean followRedirects;
    private boolean followSslRedirects;
    private long connectTimeout;
    private long writeTimeout;
    private long readTimeout;
    private boolean retryOnConnectionFailure;

    public Settings(){

        this.isCleanMetadata = false;
        this.isCleanBody = true;
        String request = PropertyUtil.getOrDefault("fastest.rest.print.request", "true");
        String response = PropertyUtil.getOrDefault("fastest.rest.print.response", "false");
        this.showRequestLog = Boolean.parseBoolean(request);
        this.showResponseLog = Boolean.parseBoolean(response);
        this.sslType = SSLType.DEFAULT;
        this.followRedirects = true;
        this.followSslRedirects = true;
        this.connectTimeout = 10000L;
        this.writeTimeout = 10000L;
        this.readTimeout = 10000L;
        this.retryOnConnectionFailure = true;
    }

    public boolean isCleanMetadata() {
        return isCleanMetadata;
    }

    public void setCleanMetadata(boolean cleanMetadata) {
        isCleanMetadata = cleanMetadata;
    }

    public boolean isCleanBody() {
        return isCleanBody;
    }

    public void setCleanBody(boolean cleanBody) {
        isCleanBody = cleanBody;
    }

    public Boolean getShowRequestLog() {
        return showRequestLog;
    }

    public void setShowRequestLog(Boolean showRequestLog) {
        this.showRequestLog = showRequestLog;
    }

    public Boolean getShowResponseLog() {
        return showResponseLog;
    }

    public void setShowResponseLog(Boolean showResponseLog) {
        this.showResponseLog = showResponseLog;
    }

    public SSLType getSslType() {
        return sslType;
    }

    public void setSslType(SSLType sslType) {
        this.sslType = sslType;
    }

    public static Settings create(){
        return new Settings();
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public boolean isFollowSslRedirects() {
        return followSslRedirects;
    }

    public void setFollowSslRedirects(boolean followSslRedirects) {
        this.followSslRedirects = followSslRedirects;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public void setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }
}
