package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.logger.FastestLoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

abstract class AbstractDefaultRequester implements Requester {
    private final static FastestLogger logger = FastestLoggerFactory.getLogger(AbstractDefaultRequester.class);
    private final Map<Object, Object> authentication = new HashMap<>();
    private final Metadata metadata;
    private final Settings settings;
    private Responder responder;

    /**
     * 需要在body中指定鉴权方式
     */
    AbstractDefaultRequester(){
        this(new HashMap<>());
    }

    /**
     * 提供一个header中的鉴权参数，则该实例对象只使用该鉴权访问
     * 建议使用这种方式，如果直接放入body中，一旦在切换鉴权信息出问题时，会出现请求结果和预期不一致的情况
     * @param authentication 鉴权map
     */
    AbstractDefaultRequester(Map<Object, Object> authentication){
        if(Objects.isNull(authentication)){
            throw new FastestBasicException("\"authentication\" cannot be null.");
        }
        this.authentication.putAll(authentication);
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
        //如果用户在构造函数中提作为鉴权供了authentication，则始终使用authentication
        if(!this.authentication.isEmpty()){
            this.authentication.forEach((k, v) -> {
                this.metadata.setHeader(String.valueOf(k), String.valueOf(v));
            });
        }
        Sender sender = new Sender(metadata, this.settings);
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
        Settings.RequesterSettings requesterSettings = this.settings.requester();
        if(requesterSettings.isCleanMetadata()){
            this.metadata.recovery();
            return;
        }
        if(requesterSettings.isCleanBody()){
            this.metadata.headersRecovery().parametersRecovery().formRecovery().jsonRecovery();
        }
    }

    @Override
    public void printResponse() {
        logger.info("**********HTTP RESPONSE**********\n" +
                "Http Status Code:{}\n" +
                "Http Response Header:{}\n" +
                "Http Response body:{}", this.responder.stateCode(), this.responder.headers(), this.responder.bodyToString());
    }
}
