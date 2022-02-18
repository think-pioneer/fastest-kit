package xyz.thinktest.fastestapi.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import xyz.thinktest.fastestapi.http.metadata.*;
import xyz.thinktest.fastestapi.utils.BulkEdit;

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
    private final Restfuls restfuls;

    private Metadata(){
        this.headers = Headers.newEmpty();
        this.parameters = Parameters.newEmpty();
        this.forms = Forms.newEmpty();
        this.json = Json.newEmpty();
        this.restfuls = Restfuls.newEmpty();
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

    public Metadata setHeader(String key, String value){
        this.headers.write(new Header(key, value));
        return this;
    }

    public Metadata setHeaders(Headers headers){
        if(CollectionUtils.isNotEmpty(headers)){
            this.headers.writeAll(headers);
        }
        return this;
    }

    public Metadata setHeaders(Header... headers){
        if(null != headers && headers.length > 0){
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

    public Metadata setHeader(Header header){
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

    public Metadata setParameter(String key, Object value){
        this.parameters.write(key, value);
        return this;
    }

    public Metadata setParameters(MetaMap parameters){
        if(MapUtils.isNotEmpty(parameters)){
            this.parameters.writeAll((Parameters) parameters);
        }
        return this;
    }

    public Metadata setParameter(String key, Parameter parameter){
        this.parameters.write(key, parameter);
        return this;
    }

    public Metadata setParameter(Parameter parameter){
        this.parameters.write(parameter.getKey(), parameter);
        return this;
    }

    public Metadata setParameters(List<Parameter> parameters){
        if(CollectionUtils.isNotEmpty(parameters)){
            parameters.forEach((parameter -> this.parameters.put(parameter.getKey(), parameter)));
        }
        return this;
    }

    public Metadata setParameters(Parameter... parameters){
        if(null != parameters && parameters.length > 0){
            this.parameters.writeAll(parameters);
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

    public Metadata setForm(Object key, Object value){
        this.forms.write(key, value);
        return this;
    }

    public Metadata setForm(Object key, Form form){
        this.forms.write(key, form);
        return this;
    }

    public Metadata setForm(Form form){
        this.forms.write(form.getKey(), form);
        return this;
    }

    public Metadata setForms(Forms forms){
        if(MapUtils.isNotEmpty(forms)){
            this.forms.writeAll(forms);
        }
        return this;
    }

    public Metadata setForms(Form... forms){
        if(null != forms && forms.length > 0){
            this.forms.writeAll(forms);
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
        this.json.erasure();
        return this;
    }

    public Metadata setRestful(String place, String value){
        this.restfuls.write(place, value);
        return this;
    }

    public Metadata setRestfuls(Restfuls restfuls){
        this.restfuls.writeAll(restfuls);
        return this;
    }

    public Metadata setRestful(Restful restful){
        this.restfuls.write(restful.getKey(), restful);
        return this;
    }

    public Metadata setRestful(String key, Restful restful){
        this.restfuls.write(key, restful);
        return this;
    }

    public Metadata setRestful(List<Restful> restfuls){
        if(CollectionUtils.isNotEmpty(restfuls)){
            restfuls.forEach(this::setRestful);
        }
        return this;
    }

    public Restfuls getRestfuls(){
        return this.restfuls;
    }

    public Metadata recovery(){
        this.url = null;
        this.method = null;
        this.headers.erasure();
        this.parameters.erasure();
        this.forms.erasure();
        this.json.erasure();
        this.restfuls.erasure();
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
                ", restfuls=" + restfuls +
                '}';
    }

    public static Metadata create(){
        return new Metadata();
    }
}
