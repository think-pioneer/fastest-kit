package org.fastest.http;

import org.fastest.common.exceptions.FastestBasicException;
import org.fastest.http.metadata.Header;
import org.fastest.http.metadata.Headers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @Date: 2020/11/15
 * @Desc: 请求者,可以理解为一个通用的请求对象(将鉴权信息直接放入body中，每次请求时带入)，也可以理解为不同的请求对象(使用Requester(Map<String, String> authentication)构造方法)
 */

public class Requester {
    private final Map<Object, Object> authentication = new HashMap<>();
    private final Metadata metadata;
    private final Settings settings;
    private Responder responder;

    public static Requester create(){
        return new Requester();
    }

    public static Requester create(Map<Object, Object> authentication){
        return new Requester(authentication);
    }

    public static Requester create(Header header){
        Map<Object, Object> authentication = new HashMap<>();
        authentication.put(header.getKey(), header.getValue());
        return create(authentication);
    }

    public static Requester create(Headers headers){
        Map<Object, Object> authentication = new HashMap<>();
        headers.forEach((header) -> authentication.put(header.getKey(), String.valueOf(header.getValue())));
        return create(authentication);
    }

    /**
     * 需要在body中指定鉴权方式
     */
    private Requester(){
        this(new HashMap<>());
    }

    /**
     * 提供一个header中的鉴权参数，则该实例对象只使用该鉴权访问
     * 建议使用这种方式，如果直接放入body中，一旦在切换鉴权信息出问题时，会出现请求结果和预期不一致的情况
     * @param authentication 鉴权map
     */
    private Requester(Map<Object, Object> authentication){
        if(Objects.isNull(authentication)){
            throw new FastestBasicException("\"authentication\" cannot be null.");
        }
        this.authentication.putAll(authentication);
        this.settings = new Settings();
        this.metadata = new Metadata();
    }

    /**
     * 设置metadata
     */
    public Metadata metadata(){
        return this.metadata;
    }

    /**
     * 设置metadata
     */
    public Metadata metadata(Metadata metadata){
        this.metadata.recovery();
        this.metadata.setUrl(metadata.getUrl());
        this.metadata.setHttpMethod(metadata.getMethod());
        this.metadata.setParameters(metadata.getParameters());
        this.metadata.setForms(metadata.getForms());
        this.metadata.setJson(metadata.getJson());
        return this.metadata;
    }

    /**
     * 客户端的设置项
     */
    public Settings settings(){
        return this.settings;
    }

    /**
     * 获取请求结果
     */
    public Responder getResponse(){
        return this.responder;
    }

    /**
     * 默认不打印response信息（某些请求的body太大），该方法手动打印
     */
    public void printResponse(){
        this.responder.respInfo();
    }

    /**
     * assert response
     */
    public Asserts asserts(){
        return this.responder.asserts;
    }

    /**
     * 同步请求
     */
    public void sync(){
        this.send(true);
    }

    /**
     * 异步请求
     */
    public void async(){
        this.send(false);
    }

    /**
     * 真正的请求操作
     * @param isSync 同步或异步
     */
    private void send(boolean isSync){
        //如果用户在构造函数中提作为鉴权供了authentication，则始终使用authentication
        if(!this.authentication.isEmpty()){
            this.authentication.forEach((k, v) -> {
                this.metadata.setHeaders(String.valueOf(k), String.valueOf(v));
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
}
