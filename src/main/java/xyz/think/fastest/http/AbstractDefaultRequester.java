package xyz.think.fastest.http;

import org.apache.commons.collections4.CollectionUtils;
import xyz.think.fastest.http.metadata.Header;
import xyz.think.fastest.http.metadata.Headers;

abstract class AbstractDefaultRequester implements Requester {
    private final Headers authentication = Headers.newEmpty();
    private final Metadata metadata;
    private final Settings settings;
    private final HttpClient httpClient;
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
        this.settings = Settings.create();
        this.metadata = Metadata.create();
        this.httpClient = HttpClient.create();
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
        this.settings = Settings.create();
        this.metadata = Metadata.create();
        this.httpClient = HttpClient.create();
    }

    @Override
    public Metadata metadata() {
        return this.metadata;
    }

    @Override
    public Requester metadata(Metadata metadata) {
        this.metadata.copy(metadata);
        return this;
    }

    @Override
    public Settings settings() {
        return this.settings;
    }

    @Override
    public Requester settings(Settings settings){
        this.settings.copy(settings);
        return this;
    }


    @Override
    public HttpClient httpClient() {
        return this.httpClient;
    }

    @Override
    public Requester httpClient(HttpClient httpClient) {
        this.httpClient.copy(httpClient);
        return this;
    }

    @Override
    public void setResponder(Responder responder){
        this.responder = responder;
    }

    @Override
    public Responder getResponder() {
        return this.responder;
    }

    @Override
    public Requester sync() {
        this.settings.setIsSync(true);
        return this;
    }

    @Override
    public Requester async() {
        this.settings.setIsSync(false);
        return this;
    }

    @Override
    public Asserts asserts() {
        return this.responder.asserts();
    }

}
