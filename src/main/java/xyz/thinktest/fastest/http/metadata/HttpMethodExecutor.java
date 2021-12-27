package xyz.thinktest.fastest.http.metadata;

import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Objects;

/**
 * @Date: 2021/12/24
 */
class HttpMethodExecutor {
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

    /**
     * http head method
     * @param builder request builder
     * @param body null
     */
    public static void head(Request.Builder builder, RequestBody body){
        builder.head();
    }

    /**
     * http patch method
     * @param builder request builder
     * @param body request body
     */
    public static void patch(Request.Builder builder, RequestBody body){
        builder.patch(body);
    }
}
