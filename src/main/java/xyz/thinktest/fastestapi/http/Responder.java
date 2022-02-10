package xyz.thinktest.fastestapi.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import xyz.thinktest.fastestapi.common.json.JSONFactory;
import xyz.thinktest.fastestapi.http.metadata.Headers;
import xyz.thinktest.fastestapi.http.metadata.Json;

import java.nio.charset.Charset;

/**
 * http response
 */
public interface Responder {

    /**
     *http响应码，非业务响应码
     */
    int stateCode();

    /**
     * http body
     */
    ResponseBody body();

    /**
     *将body转成二进制
     */
    byte[] bodyToBytes();

    /**
     * response body(string)(utf-8)
     */
    String bodyToString();

    /**
     * response body(string)自定义字符
     */
    String bodyToString(Charset charset);

    /**
     * response body to json
     */
    Json bodyToJson();

    /**
     * 获取okhttp的响应对象
     */
    Response originalResponse();

    /**
     * 获取所有响应头
     */
    Headers headers();

    /**
     *获取指定key的响应头
     * @param key header key
     * @return 指定header key的value
     */
    String header(String key);

    /**
     * 下载文件
     * @param path 文件保存路径
     */
    void download(String path);

    /**
     * 将响应填充到asserts对象中，可以直接用来进行断言
     */
    Asserts asserts();

    /**
     * 基于jackson的Java对象转换
     * 如果对象的属性结构复杂，建议使用这个方法
     */
    default <T> T bodyToObject(JavaType type){
        return JSONFactory.stringToObject(bodyToString(), type);
    }

    /**
     * 基于jackson的Java对象转换
     * 如果对象属性为基本数据类型，可以使用这个方法
     */
    default <T> T bodyToObject(Class<T> type){
        return JSONFactory.stringToObject(bodyToString(), type);
    }

    /**
     *基于jackson的Java对象转换
     * 如果对象属性也时对象，可以使用这个方法
     */
    default <T> T bodyToObject(TypeReference<T> typeReference){
        return JSONFactory.stringToObject(bodyToString(), typeReference);
    }

    /**
     *基于jackson的Java对象转换
     * 如果对象是个容器时，可以使用该方法
     */
    default <T> T bodyToObject(Class<?> collectionClass, Class<?> ...elementClasses){
        return JSONFactory.stringToObject(bodyToString(), collectionClass, elementClasses);
    }
}
