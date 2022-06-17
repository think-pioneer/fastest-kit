package xyz.thinktest.fastestapi.http;

import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import xyz.thinktest.fastestapi.http.metadata.*;
import xyz.thinktest.fastestapi.http.metadata.Headers;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author: aruba
 * @date: 2022-02-12
 */
class RequestContainer {
    private final Request.Builder builder;
    private final Metadata metadata;
    private HttpUrl httpUrl = null;
    private RequestBody requestBody = null;

    public RequestContainer(Metadata metadata){
        this.metadata = metadata;
        this.builder = new Request.Builder();
    }

    private void buildHeaders(){
        Headers headers = this.metadata.getHeaders();
        headers.forEach((obj) -> {
            Header header = (Header) obj;
            this.builder.addHeader(header.getKey(), header.getValue());
        });
    }

    private void buildQueryParameters(){
        Parameters parameters = this.metadata.getParameters();
        HttpUrl.Builder urlBuilder = this.builder.url(this.metadata.getUrl().asciiString()).build().url().newBuilder();
        if(Objects.nonNull(parameters)) {
            parameters.forEach((k, v) -> {
                ((List<Object>) v.getValue()).forEach(value -> urlBuilder.addQueryParameter(String.valueOf(k), String.valueOf(value)));
            });
        }
        this.httpUrl = urlBuilder.build();
        this.metadata.getUrl().setFullUrl(this.httpUrl.toString());
    }

    private void buildBody(){
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        Forms forms = this.metadata.getForms();
        Json json = this.metadata.getJson();
        //form和json只能存在一个如果同时存在则使用form
        if(MapUtils.isNotEmpty(forms)){
            forms.forEach((k, v) -> {
                //如果有file对象，则按照文件上传来处理
                if (v.getValue() instanceof File) {
                    File file = (File) v.getValue();
                    multipartBuilder.addFormDataPart(String.valueOf(k), file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")));
                } else {
                    multipartBuilder.addFormDataPart(String.valueOf(k), String.valueOf(v.getValue()));
                }
            });
            this.requestBody = multipartBuilder.build();
        }else if(Objects.nonNull(json)){
            this.requestBody = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        }
    }

    public Request build(){
        this.buildHeaders();
        this.buildQueryParameters();
        this.buildBody();
        this.builder.url(this.httpUrl);
        HttpMethod method = this.metadata.getMethod();
        method.execute(this.builder, this.requestBody);
        return this.builder.build();
    }
}
