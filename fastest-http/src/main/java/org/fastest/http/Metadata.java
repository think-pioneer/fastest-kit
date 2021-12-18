package org.fastest.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.fastest.http.metadata.*;
import org.fastest.utils.ObjectUtil;

import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * @Date: 2020/10/16
 * @Desc: http request metadata
 */
public class Metadata {
    private Url url;
    private HttpMethod method;
    private final Headers headers;
    private final Parameters parameters;
    private final Forms forms;
    private final Json json;

    public Metadata(){
        this.headers = new Headers();
        this.parameters = new Parameters();
        this.forms = new Forms();
        this.json = new Json();
    }

    public Metadata setUrl(Url url){
        this.url = url;
        return this;
    }

    public Metadata setUrl(String url){
        this.url = new Url(url);
        return this;
    }

    public Metadata setUrl(URL url){
        this.url = new Url(url);
        return this;
    }

    public Metadata setUrl(URI uri){
        this.url = new Url(uri);
        return this;
    }

    public Url getUrl(){
        return this.url;
    }

    public Metadata setHttpMethod(HttpMethod method){
        this.method = method;
        return this;
    }

    public HttpMethod getMethod(){
        return this.method;
    }

    public Metadata setHeaders(String key, String value){
        this.headers.write(new Header(key, value));
        return this;
    }

    public Metadata setHeaders(Headers headers){
        if(CollectionUtils.isNotEmpty(headers)){
            this.headers.writeAll(headers);
        }
        return this;
    }

    public Metadata setHeaders(List<Header> headers){
        if(CollectionUtils.isNotEmpty(headers)){
            this.headers.addAll(headers);
        }
        return this;
    }

    public Metadata setHeaders(Header header){
        this.headers.write(header);
        return this;
    }

    public Metadata setHeaders(MetaMap headers){
        if(MapUtils.isNotEmpty(headers)){
            headers.forEach((k, v) -> this.headers.add(v));
        }
        return this;
    }

    public Metadata setHeaders(String content){
        this.headers.writeAll(BulkEdit.stringToHeaders(content));
        return this;
    }

    public Headers getHeaders(){
        return this.headers;
    }

    public Metadata headersRecovery(){
        this.headers.erasure();
        return this;
    }

    public Metadata setParameters(String key, Object value){
        this.parameters.write(key, value);
        return this;
    }

    public Metadata setParameters(MetaMap parameters){
        if(MapUtils.isNotEmpty(parameters)){
            this.parameters.putAll(parameters);
        }
        return this;
    }

    public Metadata setParameters(Parameter parameter){
        this.parameters.put(parameter.getKey(), parameter);
        return this;
    }

    public Metadata setParameters(List<Parameter> parameters){
        if(CollectionUtils.isNotEmpty(parameters)){
            parameters.forEach((parameter -> this.parameters.put(parameter.getKey(), parameter)));
        }
        return this;
    }

    public Metadata setParameters(String content){
        this.parameters.writeAll(BulkEdit.stringToParameters(content));
        return this;
    }

    public Parameters getParameters(){
        return this.parameters;
    }

    public Metadata parametersRecovery(){
        this.parameters.erasure();
        return this;
    }

    public Metadata setForms(Object key, Object value){
        this.forms.write(key, value);
        return this;
    }

    public Metadata setForms(Object key, Form form){
        this.forms.put(key, form);
        return this;
    }

    public Metadata setForms(Forms forms){
        if(MapUtils.isNotEmpty(forms)){
            this.forms.putAll(forms);
        }
        return this;
    }

    public Metadata setForms(List<Form> forms){
        if(CollectionUtils.isNotEmpty(forms)){
            forms.forEach((form -> this.forms.put(form.getKey(), form)));
        }
        return this;
    }

    public Metadata setForms(String content){
        this.forms.writeAll(BulkEdit.stringToFroms(content));
        return this;
    }

    public Forms getForms(){
        return this.forms;
    }

    public Metadata formRecovery(){
        this.forms.erasure();
        return this;
    }

    public Metadata setJson(String json){
        this.json.append(json);
        return this;
    }

    public Metadata setJson(Object json){
        this.json.append(json);
        return this;
    }

    public Metadata setJson(ObjectNode json){
        this.json.append(json);
        return this;
    }

    public Metadata setJson(ArrayNode json){
        this.json.append(json);
        return this;
    }

    public Metadata setJson(JsonNode json){
        this.json.append(json);
        return this;
    }

    public Json getJson(){
        return this.json;
    }

    public Metadata jsonRecovery(){
        this.json.recovery();
        return this;
    }

    public Metadata recovery(){
        this.url = null;
        this.method = null;
        this.headers.erasure();
        this.parameters.erasure();
        this.forms.erasure();
        this.json.recovery();
        return this;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "url=" + url +
                ", method=" + method +
                ", headers=" + headers +
                ", parameters=" + parameters +
                ", forms=" + forms +
                ", json=" + json +
                '}';
    }
}
