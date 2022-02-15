package xyz.thinktest.fastestapi.http;

import org.apache.commons.collections4.CollectionUtils;
import xyz.thinktest.fastestapi.http.metadata.Header;
import xyz.thinktest.fastestapi.http.metadata.Headers;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.logger.FastestLoggerFactory;

import java.util.concurrent.TimeUnit;

abstract class AbstractDefaultRequester implements Requester {
    private final static FastestLogger logger = FastestLoggerFactory.getLogger(AbstractDefaultRequester.class);
    private final Headers authentication = Headers.newEmpty();
    private final Metadata metadata;
    private final Settings settings;
    private Responder responder;

    /**
     * 需要在body中指定鉴权方式
     */
    AbstractDefaultRequester(){
        this(Headers.newEmpty());
    }

    AbstractDefaultRequester(Header... headers){
        if(null != headers && headers.length > 0) {
            this.authentication.writeAll(headers);
        }
        AuthManager.set(this, this.authentication);
        this.settings = new Settings();
        this.metadata = new Metadata();
    }

    /**
     * 提供一个header中的鉴权参数，则该实例对象只使用该鉴权访问
     * 建议使用这种方式，如果直接放入body中，一旦在切换鉴权信息出问题时，会出现请求结果和预期不一致的情况
     * @param authentication 鉴权map
     */
    AbstractDefaultRequester(Headers authentication){
        if(CollectionUtils.isNotEmpty(authentication)) {
            this.authentication.writeAll(authentication);
        }
        AuthManager.set(this, this.authentication);
        this.settings = new Settings();
        this.metadata = new Metadata();
    }

    @Override
    public Metadata metadata() {
        return this.metadata;
    }

    @Override
    public Metadata metadata(Metadata metadata) {
        this.metadata.recovery();
        this.metadata.setUrl(metadata.getUrl());
        this.metadata.setHttpMethod(metadata.getMethod());
        this.metadata.setParameters(metadata.getParameters());
        this.metadata.setForms(metadata.getForms());
        this.metadata.setJson(metadata.getJson());
        return this.metadata;
    }

    @Override
    public Settings settings() {
        return this.settings;
    }

    @Override
    public Responder getResponder() {
        return this.responder;
    }

    @Override
    public void sync() {
        this.send(true);
    }

    @Override
    public void async() {
        this.send(false);
    }

    @Override
    public Asserts asserts() {
        return this.responder.asserts();
    }

    /**
     * 真正的请求操作
     * @param isSync 同步或异步
     */
    private void send(boolean isSync){
        HttpClientSetting builder = new HttpClientSetting();
        builder.sslSocketFactory(settings.getSslType().getSslSocketFactory(),settings.getSslType().getTrustManager())
                .followRedirects(settings.isFollowRedirects())
                .followSslRedirects(settings.isFollowRedirects())
                .connectTimeout(settings.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(settings.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(settings.getReadTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(settings.isRetryOnConnectionFailure());
        Sender sender = new Sender(metadata, builder.getClient());
        if(isSync){
            sender.sync();
        }else{
            sender.async();
        }
        this.responder = sender.getResponse();
        this.sendPost();
    }

    /**
     * 请求结束后的清理工作
     */
    private void sendPost(){
        if(this.settings.isCleanMetadata()){
            this.metadata.recovery();
            return;
        }
        if(this.settings.isCleanBody()){
            this.metadata.headersRecovery().parametersRecovery().formRecovery().jsonRecovery();
        }
    }
}
