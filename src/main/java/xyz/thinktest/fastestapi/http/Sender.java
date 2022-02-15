package xyz.thinktest.fastestapi.http;

import okhttp3.*;
import xyz.thinktest.fastestapi.common.exceptions.HttpException;
import xyz.thinktest.fastestapi.core.ApplicationBean;
import xyz.thinktest.fastestapi.http.internal.HttpCacheInternal;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 * @Date: 2020/11/15
 */
@SuppressWarnings("unchecked")
class Sender {
    private final OkHttpClient client;
    private final Request request;
    private Responder responder;
    private final HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;

    public Sender(Metadata metadata, OkHttpClient client){
        try {
            this.request = new RequestContainer(metadata).build();
        }catch (Exception e){
            throw new HttpException(e.getMessage(), e);
        }
        this.client = client;

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
        Class<Responder> responderType = httpCacheInternal.get("fastest.api.http.responder");
        this.responder = ApplicationBean.getEnhanceBean(responderType, new Class<?>[]{Response.class}, new Object[]{response});
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
}