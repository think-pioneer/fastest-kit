package org.fastest.http.metadata;

import okhttp3.Request;
import okhttp3.RequestBody;
import org.fastest.common.exceptions.FastestBasicException;
import org.fastest.utils.ObjectUtil;

import java.util.Objects;

/**
 * @Data: 2020/11/15
 * http method enum
 */
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final HttpSendable<Request.Builder, RequestBody> method;
    private final String name;

    HttpMethod(String methodName){
        this.name = methodName;
        switch (this.name){
            case "POST":
                method = MethodBuild::post;
                break;
            case "PUT":
                method = MethodBuild::put;
                break;
            case "DELETE":
                method = MethodBuild::delete;
                break;
            default:
                method = MethodBuild::get;
        }
    }

    /**
     * http method action
     * @return http method
     */
    public HttpSendable<Request.Builder, RequestBody> getMethod(){
        return method;
    }

    public static HttpMethod getMethod(String name){
        try {
            return valueOf(name);
        }catch (IllegalArgumentException e){
            throw new FastestBasicException(ObjectUtil.format("http method:[{}] not found", name));
        }
    }

    public String getName(){
        return this.name;
    }

    /**
     * build http method
     */
    static class MethodBuild {

        /**
         * http get method
         * @param builder request builder
         * @param body null
         */
        public static void get(Request.Builder builder, RequestBody body){
            builder.get();
        }

        /**
         * http post method
         * @param builder request builder
         * @param body form or json body
         */
        public static void post(Request.Builder builder, RequestBody body){
            builder.post(body);
        }

        /**
         * http put method
         * @param builder request builder
         * @param body form or json body
         */
        public static void put(Request.Builder builder, RequestBody body){
            builder.put(body);
        }

        /**
         * http delete method
         * @param builder request builder
         * @param body form or json body, or null
         */
        public static void delete(Request.Builder builder, RequestBody body){
            if(Objects.isNull(body)){
                builder.delete();
            }else{
                builder.delete(body);
            }
        }
    }
}

