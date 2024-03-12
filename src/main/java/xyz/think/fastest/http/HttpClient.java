package xyz.think.fastest.http;

import okhttp3.*;
import okhttp3.EventListener;
import okhttp3.internal.Util;
import okhttp3.internal.ws.RealWebSocket;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import xyz.think.fastest.http.ssl.SSLType;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import java.net.Proxy;
import java.net.ProxySelector;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static okhttp3.OkHttpClient.Builder;

/**
 * @author: aruba
 * @date: 2022-02-11
 * http client
 */ 
public final class HttpClient {

    private static final class Value<T>{
        private T value;
        private boolean change;

        Value(T value){
            this.value = value;
            this.change = true;
        }

        public T getValue() {
            this.change = false;
            return value;
        }

        public boolean isChange() {
            return change;
        }

        public void setValue(T value) {
            this.value = value;
            this.change = true;
        }
    }

    private Value<Dispatcher> dispatcher;
    private Value<ConnectionPool> connectionPool;
    private final List<Value<Interceptor>> interceptors;
    private final List<Value<Interceptor>> networkInterceptors;
    private Value<EventListener> eventListener;
    private Value<EventListener.Factory> eventListenerFactory;
    private Value<Boolean> retryOnConnectionFailure;
    private Value<Authenticator> authenticator;
    private Value<Boolean> followRedirects;
    private Value<Boolean> followSslRedirects;
    private Value<CookieJar> cookieJar;
    private Value<Cache> cache;
    private Value<Dns> dns;
    private Value<Proxy> proxy;
    private Value<ProxySelector> proxySelector;
    private Value<Authenticator> proxyAuthenticator;
    private Value<SocketFactory> socketFactory;
    private Value<SSLType> sslType;
    private Value<List<ConnectionSpec>> connectionSpecs;
    private Value<List<Protocol>> protocols;
    private Value<HostnameVerifier> hostnameVerifier;
    private Value<CertificatePinner> certificatePinner;
    private Value<Duration> callTimeout;
    private Value<Duration> connectTimeout;
    private Value<Duration> readTimeout;
    private Value<Duration> writeTimeout;
    private Value<Duration> pingInterval;
    private Value<Long> minWebSocketMessageToCompress;

    @FunctionalInterface
    interface Set<T>{
        void set(T t);
    }

    @FunctionalInterface
    interface SetBuilder<T>{
        void set(Builder builder, T t);
    }

    public HttpClient(){
        this.dispatcher = new Value<>(new Dispatcher());
        this.connectionPool = new Value<>(new ConnectionPool());
        this.interceptors = new ArrayList<>();
        this.networkInterceptors = new ArrayList<>();
        this.eventListener = new Value<>(new EventListener() {
            @Override
            public void cacheConditionalHit(@NotNull Call call, @NotNull Response cachedResponse) {
                super.cacheConditionalHit(call, cachedResponse);
            }
        });
        this.eventListenerFactory = new Value<>(Util.asFactory(new EventListener() {
            @Override
            public void cacheConditionalHit(@NotNull Call call, @NotNull Response cachedResponse) {
                super.cacheConditionalHit(call, cachedResponse);
            }
        }));
        this.retryOnConnectionFailure = new Value<>(true);
        this.authenticator = new Value<>(Authenticator.NONE);
        this.followRedirects = new Value<>(true);
        this.followSslRedirects = new Value<>(true);
        this.cookieJar = new Value<>(CookieJar.NO_COOKIES);
        this.cache = new Value<>(null);
        this.dns = new Value<>(Dns.SYSTEM);
        this.proxy = new Value<>(null);
        this.proxySelector = null;
        this.proxyAuthenticator = new Value<>(Authenticator.NONE);
        this.socketFactory = new Value<>(SocketFactory.getDefault());
        this.sslType = new Value<>(SSLType.DEFAULT);
        this.connectionSpecs = new Value<>(Util.immutableListOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT));
        this.protocols = new Value<>(Util.immutableListOf(Protocol.HTTP_2, Protocol.HTTP_1_1));
        this.hostnameVerifier = new Value<>(null);
        this.certificatePinner = new Value<>(CertificatePinner.DEFAULT);
        this.callTimeout = new Value<>(Duration.ofSeconds(120));
        this.connectTimeout = new Value<>(Duration.ofSeconds(60));
        this.readTimeout = new Value<>(Duration.ofSeconds(60));
        this.writeTimeout = new Value<>(Duration.ofSeconds(60));
        this.pingInterval = new Value<>(Duration.ofSeconds(0));
        this.minWebSocketMessageToCompress = new Value<>(RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE);
    }

    OkHttpClient build(){
        Builder builder = new Builder();
        this.set(builder::dispatcher, this.dispatcher);
        this.set(builder::connectionPool, this.connectionPool);
        this.setCollections(this.interceptors, builder::addInterceptor);
        this.setCollections(this.networkInterceptors, builder::addNetworkInterceptor);
        this.set(builder::eventListener, this.eventListener);
        this.set(builder::eventListenerFactory, this.eventListenerFactory);
        this.set(builder::retryOnConnectionFailure, this.retryOnConnectionFailure);
        this.set(builder::authenticator, this.authenticator);
        this.set(builder::followRedirects, this.followRedirects);
        this.set(builder::followSslRedirects, this.followSslRedirects);
        this.set(builder::cookieJar, this.cookieJar);
        this.set(builder::cache, this.cache);
        this.set(builder::dns, this.dns);
        this.set(builder::proxy, this.proxy);
        this.set(builder::proxySelector, this.proxySelector);
        this.set(builder::proxyAuthenticator, this.proxyAuthenticator);
        this.set(builder::socketFactory, this.socketFactory);
        this.set(this::setSslSocketFactory, builder, this.sslType);
        this.set(builder::connectionSpecs, this.connectionSpecs);
        this.set(builder::protocols, this.protocols);
        this.set(builder::hostnameVerifier, this.hostnameVerifier);
        this.set(builder::certificatePinner, this.certificatePinner);
        this.set(builder::callTimeout, this.callTimeout);
        this.set(builder::connectTimeout, this.connectTimeout);
        this.set(builder::readTimeout, this.readTimeout);
        this.set(builder::writeTimeout, this.writeTimeout);
        this.set(builder::pingInterval, this.pingInterval);
        this.set(builder::minWebSocketMessageToCompress, this.minWebSocketMessageToCompress);
        return builder.build();
    }

    private <T> void set(SetBuilder<T> set, Builder builder, Value<T> value) {
        if (Objects.nonNull(value) && value.isChange() && Objects.nonNull(value.getValue())){
            set.set(builder, value.getValue());
        }
    }

    private <T> void set(Set<T> set, Value<T> value){
        if (Objects.nonNull(value) && value.isChange() && Objects.nonNull(value.getValue())){
            set.set(value.getValue());
        }
    }

    private <T> void setCollections(Collection<Value<T>> collection, Consumer<? super T> action){
        if (CollectionUtils.isNotEmpty(collection)){
            collection.forEach(tValue -> action.accept(tValue.getValue()));
        }
    }

    private <T extends SSLType> void setSslSocketFactory(Builder builder, T sslType){
        builder.sslSocketFactory(sslType.getSslSocketFactory(), sslType.getTrustManager());
    }

    public HttpClient dispatcher(Dispatcher dispatcher){
        this.dispatcher = new Value<>(dispatcher);
        return this;
    }

    public HttpClient connectionPool(ConnectionPool connectionPool){
        this.connectionPool = new Value<>(connectionPool);
        return this;
    }

    public HttpClient addInterceptors(Interceptor... interceptors){
        this.interceptors.addAll(Stream.of(interceptors).map(Value::new).collect(Collectors.toList()));
        return this;
    }

    public HttpClient addInterceptors(List<Interceptor> interceptors){
        this.interceptors.addAll(interceptors.stream().map(Value::new).collect(Collectors.toList()));
        return this;
    }

    public HttpClient addNetworkInterceptor(Interceptor... interceptors){
        this.networkInterceptors.addAll(Stream.of(interceptors).map(Value::new).collect(Collectors.toList()));
        return this;
    }
    public HttpClient addNetworkInterceptor(List<Interceptor> interceptors){
        this.networkInterceptors.addAll(interceptors.stream().map(Value::new).collect(Collectors.toList()));
        return this;
    }

    public HttpClient eventListener(EventListener eventListener){
        this.eventListener = new Value<>(eventListener);
        return this;
    }

    public HttpClient eventListenerFactory(EventListener.Factory factory){
        this.eventListenerFactory = new Value<>(factory);
        return this;
    }

    public HttpClient retryOnConnectionFailure(Boolean retryOnConnectionFailure){
        this.retryOnConnectionFailure = new Value<>(retryOnConnectionFailure);
        return this;
    }

    public HttpClient authenticator(Authenticator authenticator){
        this.authenticator = new Value<>(authenticator);
        return this;
    }

    public HttpClient followRedirects(Boolean followRedirects){
        this.followRedirects = new Value<>(followRedirects);
        return this;
    }

    public HttpClient followSslRedirects(Boolean followSslRedirects){
        this.followSslRedirects = new Value<>(followSslRedirects);
        return this;
    }

    public HttpClient cookieJar(CookieJar cookieJar){
        this.cookieJar = new Value<>(cookieJar);
        return this;
    }

    public HttpClient cache(Cache cache){
        this.cache = new Value<>(cache);
        return this;
    }

    public HttpClient dns(Dns dns){
        this.dns = new Value<>(dns);
        return this;
    }

    public HttpClient proxy(Proxy proxy){
        this.proxy = new Value<>(proxy);
        return this;
    }

    public HttpClient proxySelector(ProxySelector proxySelector){
        this.proxySelector = new Value<>(proxySelector);
        return this;
    }

    public HttpClient proxyAuthenticator(Authenticator proxyAuthenticator){
        this.proxyAuthenticator = new Value<>(proxyAuthenticator);
        return this;
    }

    public HttpClient socketFactory(SocketFactory socketFactory){
        this.socketFactory = new Value<>(socketFactory);
        return this;
    }

    public HttpClient sslType(SSLType sslType){
        this.sslType = new Value<>(sslType);
        return this;
    }

    public HttpClient connectionSpecs(List<ConnectionSpec> connectionSpecs){
        this.connectionSpecs = new Value<>(connectionSpecs);
        return this;
    }

    public HttpClient protocols(List<Protocol> protocols){
        this.protocols = new Value<>(protocols);
        return this;
    }

    public HttpClient hostnameVerifier(HostnameVerifier hostnameVerifier){
        this.hostnameVerifier = new Value<>(hostnameVerifier);
        return this;
    }

    public HttpClient certificatePinner(CertificatePinner certificatePinner){
        this.certificatePinner = new Value<>(certificatePinner);
        return this;
    }

    public HttpClient callTimeout(Duration callTimeout){
        this.callTimeout = new Value<>(callTimeout);
        return this;
    }

    public HttpClient connectTimeout(Duration connectTimeout){
        this.connectTimeout = new Value<>(connectTimeout);
        return this;
    }


    public HttpClient readTimeout(Duration readTimeout){
        this.readTimeout = new Value<>(readTimeout);
        return this;
    }

    public HttpClient writeTimeout(Duration writeTimeout){
        this.writeTimeout = new Value<>(writeTimeout);
        return this;
    }

    public HttpClient pingInterval(Duration pingInterval){
        this.pingInterval = new Value<>(pingInterval);
        return this;
    }

    public HttpClient minWebSocketMessageToCompress(Long bytes){
        this.minWebSocketMessageToCompress = new Value<>(bytes);
        return this;
    }

    public void copy(HttpClient httpClient){
        this.dispatcher(httpClient.dispatcher.value);
        this.connectionPool(httpClient.connectionPool.value);
        this.addInterceptors(httpClient.interceptors.stream().map(value -> value.value).collect(Collectors.toList()));
        this.addNetworkInterceptor(httpClient.networkInterceptors.stream().map(value -> value.value).collect(Collectors.toList()));
        this.eventListener(httpClient.eventListener.value);
        this.eventListenerFactory(httpClient.eventListenerFactory.value);
        this.retryOnConnectionFailure(httpClient.retryOnConnectionFailure.value);
        this.authenticator(httpClient.authenticator.value);
        this.followRedirects(httpClient.followRedirects.value);
        this.followSslRedirects(httpClient.followSslRedirects.value);
        this.cookieJar(httpClient.cookieJar.value);
        this.cache(httpClient.cache.value);
        this.dns(httpClient.dns.value);
        this.proxy(httpClient.proxy.value);
        this.proxySelector(httpClient.proxySelector.value);
        this.proxyAuthenticator(httpClient.proxyAuthenticator.value);
        this.socketFactory(httpClient.socketFactory.value);
        this.sslType(httpClient.sslType.value);
        this.connectionSpecs(httpClient.connectionSpecs.value);
        this.protocols(httpClient.protocols.value);
        this.hostnameVerifier(httpClient.hostnameVerifier.value);
        this.certificatePinner(httpClient.certificatePinner.value);
        this.callTimeout(httpClient.callTimeout.value);
        this.connectTimeout(httpClient.connectTimeout.value);
        this.readTimeout(httpClient.readTimeout.value);
        this.writeTimeout(httpClient.writeTimeout.value);
        this.pingInterval(httpClient.pingInterval.value);
        this.minWebSocketMessageToCompress(httpClient.minWebSocketMessageToCompress.value);
    }

    public static HttpClient create(){
        return new HttpClient();
    }
}
