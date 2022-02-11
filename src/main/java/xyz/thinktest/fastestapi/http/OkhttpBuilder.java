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
class OkhttpBuilder {
    private final OkHttpClient.Builder builder;

    public OkhttpBuilder(){
        this.builder = new OkHttpClient.Builder();
    }

    public OkHttpClient build(){
        return this.builder.build();
    }

    public OkhttpBuilder dispatcher(Dispatcher dispatcher){
        this.builder.dispatcher(dispatcher);
        return this;
    }

    public OkhttpBuilder connectionPool(ConnectionPool connectionPool){
        this.builder.connectionPool(connectionPool);
        return this;
    }

    public OkhttpBuilder addInterceptors(Interceptor interceptor){
        this.builder.addInterceptor(interceptor);
        return this;
    }

    public OkhttpBuilder addNetworkInterceptor(Interceptor interceptor){
        this.builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public OkhttpBuilder eventListener(EventListener eventListener){
        this.builder.eventListener(eventListener);
        return this;
    }

    public OkhttpBuilder eventListenerFactory(EventListener.Factory factory){
        this.builder.eventListenerFactory(factory);
        return this;
    }

    public OkhttpBuilder retryOnConnectionFailure(Boolean retryOnConnectionFailure){
        this.builder.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }

    public OkhttpBuilder authenticator(Authenticator authenticator){
        this.builder.authenticator(authenticator);
        return this;
    }

    public OkhttpBuilder followRedirects(Boolean followRedirects){
        this.builder.followRedirects(followRedirects);
        return this;
    }

    public OkhttpBuilder followSslRedirects(Boolean followProtocolRedirects){
        this.builder.followSslRedirects(followProtocolRedirects);
        return this;
    }

    public OkhttpBuilder cookieJar(CookieJar cookieJar){
        this.builder.cookieJar(cookieJar);
        return this;
    }

    public OkhttpBuilder cache(Cache cache){
        this.builder.cache(cache);
        return this;
    }

    public OkhttpBuilder dns(Dns dns){
        this.builder.dns(dns);
        return this;
    }

    public OkhttpBuilder proxy(Proxy proxy){
        this.builder.proxy(proxy);
        return this;
    }

    public OkhttpBuilder proxySelector(ProxySelector proxySelector){
        this.builder.proxySelector(proxySelector);
        return this;
    }

    public OkhttpBuilder proxyAuthenticator(Authenticator proxyAuthenticator){
        this.builder.proxyAuthenticator(proxyAuthenticator);
        return this;
    }

    public OkhttpBuilder socketFactory(SocketFactory socketFactory){
        this.builder.socketFactory(socketFactory);
        return this;
    }

    @Deprecated
    public OkhttpBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory){
        this.builder.sslSocketFactory(sslSocketFactory);
        return this;
    }

    public OkhttpBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager){
        this.builder.sslSocketFactory(sslSocketFactory, trustManager);
        return this;
    }

    public OkhttpBuilder connectionSpecs(List<ConnectionSpec> connectionSpecs){
        this.builder.connectionSpecs(connectionSpecs);
        return this;
    }

    public OkhttpBuilder protocols(List<Protocol> protocols){
        this.builder.protocols(protocols);
        return this;
    }

    public OkhttpBuilder hostnameVerifier(HostnameVerifier hostnameVerifier){
        this.builder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    public OkhttpBuilder certificatePinner(CertificatePinner certificatePinner){
        this.builder.certificatePinner(certificatePinner);
        return this;
    }

    public OkhttpBuilder callTimeout(Long timeOut, TimeUnit unit){
        this.builder.callTimeout(timeOut, unit);
        return this;
    }

    public OkhttpBuilder callTimeout(Duration duration){
        this.builder.callTimeout(duration);
        return this;
    }

    public OkhttpBuilder connectTimeout(Long timeout, TimeUnit unit){
        this.builder.connectTimeout(timeout, unit);
        return this;
    }

    public OkhttpBuilder connectTimeout(Duration duration){
        this.builder.connectTimeout(duration);
        return this;
    }

    public OkhttpBuilder readTimeout(Long timeout, TimeUnit unit){
        this.builder.readTimeout(timeout, unit);
        return this;
    }

    public OkhttpBuilder readTimeout(Duration duration){
        this.builder.readTimeout(duration);
        return this;
    }

    public OkhttpBuilder writeTimeout(Long timeout, TimeUnit unit){
        this.builder.writeTimeout(timeout, unit);
        return this;
    }

    public OkhttpBuilder writeTimeout(Duration duration){
        this.builder.writeTimeout(duration);
        return this;
    }

    public OkhttpBuilder pingInterval(Long timeout, TimeUnit unit){
        this.builder.pingInterval(timeout, unit);
        return this;
    }

    public OkhttpBuilder pingInterval(Duration duration){
        this.builder.pingInterval(duration);
        return this;
    }

    public OkhttpBuilder minWebSocketMessageToCompress(Long bytes){
        this.builder.minWebSocketMessageToCompress(bytes);
        return this;
    }
}
