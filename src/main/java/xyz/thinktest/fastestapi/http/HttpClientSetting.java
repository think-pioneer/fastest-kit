package xyz.thinktest.fastestapi.http;

import okhttp3.*;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.net.Proxy;
import java.net.ProxySelector;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: aruba
 * @date: 2022-02-11
 * 剥离出okhttp的client，提供一个专门用于对外的设置对象
 */ 
class HttpClientSetting {
    private final OkHttpClient.Builder builder;

    public HttpClientSetting(){
        this.builder = new OkHttpClient.Builder();
    }

    public OkHttpClient getClient(){
        return this.builder.build();
    }

    public HttpClientSetting dispatcher(Dispatcher dispatcher){
        this.builder.dispatcher(dispatcher);
        return this;
    }

    public HttpClientSetting connectionPool(ConnectionPool connectionPool){
        this.builder.connectionPool(connectionPool);
        return this;
    }

    public HttpClientSetting addInterceptors(Interceptor interceptor){
        this.builder.addInterceptor(interceptor);
        return this;
    }

    public HttpClientSetting addNetworkInterceptor(Interceptor interceptor){
        this.builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public HttpClientSetting eventListener(EventListener eventListener){
        this.builder.eventListener(eventListener);
        return this;
    }

    public HttpClientSetting eventListenerFactory(EventListener.Factory factory){
        this.builder.eventListenerFactory(factory);
        return this;
    }

    public HttpClientSetting retryOnConnectionFailure(Boolean retryOnConnectionFailure){
        this.builder.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }

    public HttpClientSetting authenticator(Authenticator authenticator){
        this.builder.authenticator(authenticator);
        return this;
    }

    public HttpClientSetting followRedirects(Boolean followRedirects){
        this.builder.followRedirects(followRedirects);
        return this;
    }

    public HttpClientSetting followSslRedirects(Boolean followProtocolRedirects){
        this.builder.followSslRedirects(followProtocolRedirects);
        return this;
    }

    public HttpClientSetting cookieJar(CookieJar cookieJar){
        this.builder.cookieJar(cookieJar);
        return this;
    }

    public HttpClientSetting cache(Cache cache){
        this.builder.cache(cache);
        return this;
    }

    public HttpClientSetting dns(Dns dns){
        this.builder.dns(dns);
        return this;
    }

    public HttpClientSetting proxy(Proxy proxy){
        this.builder.proxy(proxy);
        return this;
    }

    public HttpClientSetting proxySelector(ProxySelector proxySelector){
        this.builder.proxySelector(proxySelector);
        return this;
    }

    public HttpClientSetting proxyAuthenticator(Authenticator proxyAuthenticator){
        this.builder.proxyAuthenticator(proxyAuthenticator);
        return this;
    }

    public HttpClientSetting socketFactory(SocketFactory socketFactory){
        this.builder.socketFactory(socketFactory);
        return this;
    }

    @Deprecated
    public HttpClientSetting sslSocketFactory(SSLSocketFactory sslSocketFactory){
        this.builder.sslSocketFactory(sslSocketFactory);
        return this;
    }

    public HttpClientSetting sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager){
        this.builder.sslSocketFactory(sslSocketFactory, trustManager);
        return this;
    }

    public HttpClientSetting connectionSpecs(List<ConnectionSpec> connectionSpecs){
        this.builder.connectionSpecs(connectionSpecs);
        return this;
    }

    public HttpClientSetting protocols(List<Protocol> protocols){
        this.builder.protocols(protocols);
        return this;
    }

    public HttpClientSetting hostnameVerifier(HostnameVerifier hostnameVerifier){
        this.builder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    public HttpClientSetting certificatePinner(CertificatePinner certificatePinner){
        this.builder.certificatePinner(certificatePinner);
        return this;
    }

    public HttpClientSetting callTimeout(Long timeOut, TimeUnit unit){
        this.builder.callTimeout(timeOut, unit);
        return this;
    }

    public HttpClientSetting callTimeout(Duration duration){
        this.builder.callTimeout(duration);
        return this;
    }

    public HttpClientSetting connectTimeout(Long timeout, TimeUnit unit){
        this.builder.connectTimeout(timeout, unit);
        return this;
    }

    public HttpClientSetting connectTimeout(Duration duration){
        this.builder.connectTimeout(duration);
        return this;
    }

    public HttpClientSetting readTimeout(Long timeout, TimeUnit unit){
        this.builder.readTimeout(timeout, unit);
        return this;
    }

    public HttpClientSetting readTimeout(Duration duration){
        this.builder.readTimeout(duration);
        return this;
    }

    public HttpClientSetting writeTimeout(Long timeout, TimeUnit unit){
        this.builder.writeTimeout(timeout, unit);
        return this;
    }

    public HttpClientSetting writeTimeout(Duration duration){
        this.builder.writeTimeout(duration);
        return this;
    }

    public HttpClientSetting pingInterval(Long timeout, TimeUnit unit){
        this.builder.pingInterval(timeout, unit);
        return this;
    }

    public HttpClientSetting pingInterval(Duration duration){
        this.builder.pingInterval(duration);
        return this;
    }

    public HttpClientSetting minWebSocketMessageToCompress(Long bytes){
        this.builder.minWebSocketMessageToCompress(bytes);
        return this;
    }
}
