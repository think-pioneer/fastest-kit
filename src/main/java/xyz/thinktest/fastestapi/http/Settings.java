package xyz.thinktest.fastestapi.http;

import okhttp3.*;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.net.Proxy;
import java.net.ProxySelector;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2020/10/24
 */
public class Settings extends OkHttpClient {
    private final Builder builder;
    private final RequesterSettings requesterSettings;

    Settings(){
        builder = new Builder();
        requesterSettings = new RequesterSettings();
    }

    OkHttpClient http(){
        return builder.build();
    }

    public RequesterSettings requester(){
        return requesterSettings;
    }

    public Settings cleanMetadata(boolean isCleanMetadata){
        this.requesterSettings.setCleanMetadata(isCleanMetadata);
        return this;
    }

    public Settings cleanBody(boolean isCleanBody){
        this.requesterSettings.setCleanBody(isCleanBody);
        return this;
    }

    public Settings showRequestLog(boolean showRequestLog){
        this.requesterSettings.setShowRequestLog(showRequestLog);
        return this;
    }

    public Settings showResponseLog(boolean showResponseLog){
        this.requesterSettings.setShowResponseLog(showResponseLog);
        return this;
    }

    public Settings dispatcher(Dispatcher dispatcher){
        this.builder.dispatcher(dispatcher);
        return this;
    }

    public Settings connectionPool(ConnectionPool connectionPool){
        this.builder.connectionPool(connectionPool);
        return this;
    }

    public Settings addInterceptors(Interceptor interceptor){
        this.builder.addInterceptor(interceptor);
        return this;
    }

    public Settings addNetworkInterceptor(Interceptor interceptor){
        this.builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public Settings eventListener(EventListener eventListener){
        this.builder.eventListener(eventListener);
        return this;
    }

    public Settings eventListenerFactory(EventListener.Factory factory){
        this.builder.eventListenerFactory(factory);
        return this;
    }

    public Settings retryOnConnectionFailure(Boolean retryOnConnectionFailure){
        this.builder.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }

    public Settings authenticator(Authenticator authenticator){
        this.builder.authenticator(authenticator);
        return this;
    }

    public Settings followRedirects(Boolean followRedirects){
        this.builder.followRedirects(followRedirects);
        return this;
    }

    public Settings followSslRedirects(Boolean followProtocolRedirects){
        this.builder.followSslRedirects(followProtocolRedirects);
        return this;
    }

    public Settings cookieJar(CookieJar cookieJar){
        this.builder.cookieJar(cookieJar);
        return this;
    }

    public Settings cache(Cache cache){
        this.builder.cache(cache);
        return this;
    }

    public Settings dns(Dns dns){
        this.builder.dns(dns);
        return this;
    }

    public Settings proxy(Proxy proxy){
        this.builder.proxy(proxy);
        return this;
    }

    public Settings proxySelector(ProxySelector proxySelector){
        this.builder.proxySelector(proxySelector);
        return this;
    }

    public Settings proxyAuthenticator(Authenticator proxyAuthenticator){
        this.builder.proxyAuthenticator(proxyAuthenticator);
        return this;
    }

    public Settings socketFactory(SocketFactory socketFactory){
        this.builder.socketFactory(socketFactory);
        return this;
    }

    @Deprecated
    public Settings sslSocketFactory(SSLSocketFactory sslSocketFactory){
        this.builder.sslSocketFactory(sslSocketFactory);
        return this;
    }

    public Settings sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager){
        this.builder.sslSocketFactory(sslSocketFactory, trustManager);
        return this;
    }

    public Settings connectionSpecs(List<ConnectionSpec> connectionSpecs){
        this.builder.connectionSpecs(connectionSpecs);
        return this;
    }

    public Settings protocols(List<Protocol> protocols){
        this.builder.protocols(protocols);
        return this;
    }

    public Settings hostnameVerifier(HostnameVerifier hostnameVerifier){
        this.builder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    public Settings certificatePinner(CertificatePinner certificatePinner){
        this.builder.certificatePinner(certificatePinner);
        return this;
    }

    public Settings callTimeout(Long timeOut, TimeUnit unit){
        this.builder.callTimeout(timeOut, unit);
        return this;
    }

    public Settings callTimeout(Duration duration){
        this.builder.callTimeout(duration);
        return this;
    }

    public Settings connectTimeout(Long timeout, TimeUnit unit){
        this.builder.connectTimeout(timeout, unit);
        return this;
    }

    public Settings connectTimeout(Duration duration){
        this.builder.connectTimeout(duration);
        return this;
    }

    public Settings readTimeout(Long timeout, TimeUnit unit){
        this.builder.readTimeout(timeout, unit);
        return this;
    }

    public Settings readTimeout(Duration duration){
        this.builder.readTimeout(duration);
        return this;
    }

    public Settings writeTimeout(Long timeout, TimeUnit unit){
        this.builder.writeTimeout(timeout, unit);
        return this;
    }

    public Settings writeTimeout(Duration duration){
        this.builder.writeTimeout(duration);
        return this;
    }

    public Settings pingInterval(Long timeout, TimeUnit unit){
        this.builder.pingInterval(timeout, unit);
        return this;
    }

    public Settings pingInterval(Duration duration){
        this.builder.pingInterval(duration);
        return this;
    }

    public Settings minWebSocketMessageToCompress(Long bytes){
        this.builder.minWebSocketMessageToCompress(bytes);
        return this;
    }

    public static class RequesterSettings {
        private boolean isCleanMetadata;
        private boolean isCleanBody;
        private Boolean showRequestLog;
        private Boolean showResponseLog;

        RequesterSettings(){
            this.isCleanMetadata = false;
            this.isCleanBody = true;
            String request = PropertyUtil.getOrDefault("fastest.rest.print.request", "true");
            String response = PropertyUtil.getOrDefault("fastest.rest.print.response", "false");
            this.showRequestLog = Boolean.parseBoolean(request);
            this.showResponseLog = Boolean.parseBoolean(response);
        }

        public boolean isCleanMetadata() {
            return isCleanMetadata;
        }

        void setCleanMetadata(boolean cleanMetadata) {
            isCleanMetadata = cleanMetadata;
        }

        public boolean isCleanBody() {
            return isCleanBody;
        }

        void setCleanBody(boolean cleanBody) {
            isCleanBody = cleanBody;
        }

        public boolean isShowRequestLog() {
            return showRequestLog;
        }

        void setShowRequestLog(boolean showRequestLog) {
            this.showRequestLog = showRequestLog;
        }

        public boolean isShowResponseLog() {
            return showResponseLog;
        }

        void setShowResponseLog(boolean showResponseLog) {
            this.showResponseLog = showResponseLog;
        }
    }
}
