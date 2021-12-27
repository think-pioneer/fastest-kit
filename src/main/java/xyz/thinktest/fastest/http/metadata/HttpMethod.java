package xyz.thinktest.fastest.http.metadata;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @Date: 2021/12/24
 */
public enum HttpMethod {
    GET("GET"){
        @Override
        public void execute(Request.Builder builder, RequestBody body) {
            HttpSendable<Request.Builder, RequestBody> httpSendable = HttpMethodExecutor::get;
            httpSendable.run(builder, body);
        }
    },
    HEAD("HEAD"){
        @Override
        public void execute(Request.Builder builder, RequestBody body) {
            HttpSendable<Request.Builder, RequestBody> httpSendable = HttpMethodExecutor::head;
            httpSendable.run(builder, body);
        }
    },
    POST("POST"){
        @Override
        public void execute(Request.Builder builder, RequestBody body) {
            HttpSendable<Request.Builder, RequestBody> httpSendable = HttpMethodExecutor::post;
            httpSendable.run(builder, body);
        }
    },
    DELETE("DELETE"){
        @Override
        public void execute(Request.Builder builder, RequestBody body) {
            HttpSendable<Request.Builder, RequestBody> httpSendable = HttpMethodExecutor::delete;
            httpSendable.run(builder, body);
        }
    },
    PUT("PUT"){
        @Override
        public void execute(Request.Builder builder, RequestBody body) {
            HttpSendable<Request.Builder, RequestBody> httpSendable = HttpMethodExecutor::put;
            httpSendable.run(builder, body);
        }
    },
    PATCH("PATCH"){
        @Override
        public void execute(Request.Builder builder, RequestBody body) {
            HttpSendable<Request.Builder, RequestBody> httpSendable = HttpMethodExecutor::patch;
            httpSendable.run(builder, body);
        }
    };

    private final String methodName;
    HttpMethod(String methodName){
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public abstract void execute(Request.Builder builder, RequestBody body);
}
