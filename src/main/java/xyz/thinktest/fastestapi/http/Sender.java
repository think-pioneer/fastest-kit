package xyz.thinktest.fastestapi.http;

import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import xyz.thinktest.fastestapi.common.exceptions.HttpException;
import xyz.thinktest.fastestapi.http.metadata.*;
import xyz.thinktest.fastestapi.http.metadata.Headers;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.logger.FastestLoggerFactory;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @Date: 2020/11/15
 */

class Sender {
    private static final FastestLogger logger = FastestLoggerFactory.getLogger(Sender.class);
    private final OkHttpClient client;
    private final Request request;
    private Responder responder;

    public Sender(Metadata metadata, Settings settings){
        try {
            this.request = new RequestContainer(metadata).build();
        }catch (Exception e){
            throw new HttpException(e.getMessage());
        }
        this.client = settings.client();

    }

    /**
     * get response
     * @return response
     */
    public Responder getResponse(){
        return this.responder;
    }

    /**
     * set response when async request
     * @param response response
     */
    private void setResponse(Response response){
        this.responder = new Responder(response);
    }

    /**
     * sync request
     */
    public void sync(){
        try {
            Response response = this.client.newCall(this.request).execute();
            this.setResponse(response);
        }catch (Exception e){
            throw new HttpException(ObjectUtil.format("send sync request error:{}", e.getMessage()), e.getCause());
        }
    }

    /**
     * async request
     */
    public void async(){
        try {
            Call call = this.client.newCall(this.request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    setResponse(response);
                }
            });
        }catch (Exception e){
            throw new HttpException("send async request error", e);
        }
    }

    static class RequestContainer {
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
                    urlBuilder.addQueryParameter(String.valueOf(k), String.valueOf(v.getValue()));
                });
            }
            this.httpUrl = urlBuilder.build();
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
            logger.info("**********HTTP REQUEST**********\n" +
                    "Http Url:{}\n" +
                    "Http Method:{}\n" +
                    "Http Header:{}\n" +
                    "Http QueryParameters:{}\n" +
                    "Http Forms:{}\n" +
                    "Http Json:{}", this.httpUrl, method.getMethodName(), this.metadata.getHeaders(), this.metadata.getParameters(), this.metadata.getForms(), this.metadata.getJson());
            method.execute(this.builder, this.requestBody);
            return this.builder.build();
        }
    }
}