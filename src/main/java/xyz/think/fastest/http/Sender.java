package xyz.think.fastest.http;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import xyz.think.fastest.common.exceptions.HttpException;
import xyz.think.fastest.utils.string.StringUtils;

import java.io.IOException;

/**
 *
 * @Date: 2020/11/15
 */
@SuppressWarnings("unchecked")
class Sender {
    private final OkHttpClient client;
    private final Request request;
    private Response response;

    public Sender(Requester requester){
        try {
            this.request = new RequestContainer(requester.metadata()).build();
        }catch (Exception e){
            throw new HttpException(e.getMessage(), e);
        }
        this.client = requester.httpClient().build();

    }

    /**
     * get response
     * @return response
     */
    public Response getResponse(){
        return this.response;
    }

    /**
     * sync request
     */
    public void sync(){
        try {
            this.response = this.client.newCall(this.request).execute();
        }catch (Exception e){
            throw new HttpException(StringUtils.format("send sync request error:{0}", e.getMessage()), e.getCause());
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
                    Sender.this.response = response;
                }
            });
        }catch (Exception e){
            throw new HttpException("send async request error", e);
        }
    }
}