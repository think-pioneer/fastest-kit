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
class HttpClient {
    private final OkHttpClient.Builder builder;

    public HttpClient(){
        this.builder = new OkHttpClient.Builder();
    }

    public OkHttpClient getClient(){
        return this.builder.build();
    }

    public HttpClient dispatcher(Dispatcher dispatcher){
        this.builder.dispatcher(dispatcher);
        return this;
    }

    public HttpClient connectionPool(ConnectionPool connectionPool){
        this.builder.connectionPool(connectionPool);
        return this;
    }

    public HttpClient addInterceptors(Interceptor interceptor){
        this.builder.addInterceptor(interceptor);
        return this;
    }

    public HttpClient addNetworkInterceptor(Interceptor interceptor){
        this.builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public HttpClient eventListener(EventListener eventListener){
        this.builder.eventListener(eventListener);
        return this;
    }

    public HttpClient eventListenerFactory(EventListener.Factory factory){
        this.builder.eventListenerFactory(factory);
        return this;
    }

    public HttpClient retryOnConnectionFailure(Boolean retryOnConnectionFailure){
        this.builder.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }

    public HttpClient authenticator(Authenticator authenticator){
        this.builder.authenticator(authenticator);
        return this;
    }

    public HttpClient followRedirects(Boolean followRedirects){
        this.builder.followRedirects(followRedirects);
        return this;
    }

    public HttpClient followSslRedirects(Boolean followProtocolRedirects){
        this.builder.followSslRedirects(followProtocolRedirects);
        return this;
    }

    public HttpClient cookieJar(CookieJar cookieJar){
        this.builder.cookieJar(cookieJar);
        return this;
    }

    public HttpClient cache(Cache cache){
        this.builder.cache(cache);
        return this;
    }

    public HttpClient dns(Dns dns){
        this.builder.dns(dns);
        return this;
    }

    public HttpClient proxy(Proxy proxy){
        this.builder.proxy(proxy);
        return this;
    }

    public HttpClient proxySelector(ProxySelector proxySelector){
        this.builder.proxySelector(proxySelector);
        return this;
    }

    public HttpClient proxyAuthenticator(Authenticator proxyAuthenticator){
        this.builder.proxyAuthenticator(proxyAuthenticator);
        return this;
    }

    public HttpClient socketFactory(SocketFactory socketFactory){
        this.builder.socketFactory(socketFactory);
        return this;
    }

    @Deprecated
    public HttpClient sslSocketFactory(SSLSocketFactory sslSocketFactory){
        this.builder.sslSocketFactory(sslSocketFactory);
        return this;
    }

    public HttpClient sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager){
        this.builder.sslSocketFactory(sslSocketFactory, trustManager);
        return this;
    }

    public HttpClient connectionSpecs(List<ConnectionSpec> connectionSpecs){
        this.builder.connectionSpecs(connectionSpecs);
        return this;
    }

    public HttpClient protocols(List<Protocol> protocols){
        this.builder.protocols(protocols);
        return this;
    }

    public HttpClient hostnameVerifier(HostnameVerifier hostnameVerifier){
        this.builder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    public HttpClient certificatePinner(CertificatePinner certificatePinner){
        this.builder.certificatePinner(certificatePinner);
        return this;
    }

    public HttpClient callTimeout(Long timeOut, TimeUnit unit){
        this.builder.callTimeout(timeOut, unit);
        return this;
    }

    public HttpClient callTimeout(Duration duration){
        this.builder.callTimeout(duration);
        return this;
    }

    public HttpClient connectTimeout(Long timeout, TimeUnit unit){
        this.builder.connectTimeout(timeout, unit);
        return this;
    }

    public HttpClient connectTimeout(Duration duration){
        this.builder.connectTimeout(duration);
        return this;
    }

    public HttpClient readTimeout(Long timeout, TimeUnit unit){
        this.builder.readTimeout(timeout, unit);
        return this;
    }

    public HttpClient readTimeout(Duration duration){
        this.builder.readTimeout(duration);
        return this;
    }

    public HttpClient writeTimeout(Long timeout, TimeUnit unit){
        this.builder.writeTimeout(timeout, unit);
        return this;
    }

    public HttpClient writeTimeout(Duration duration){
        this.builder.writeTimeout(duration);
        return this;
    }

    public HttpClient pingInterval(Long timeout, TimeUnit unit){
        this.builder.pingInterval(timeout, unit);
        return this;
    }

    public HttpClient pingInterval(Duration duration){
        this.builder.pingInterval(duration);
        return this;
    }

    public HttpClient minWebSocketMessageToCompress(Long bytes){
        this.builder.minWebSocketMessageToCompress(bytes);
        return this;
    }
}
