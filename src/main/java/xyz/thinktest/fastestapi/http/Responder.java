package xyz.thinktest.fastestapi.http;

import okhttp3.Response;
import okhttp3.ResponseBody;
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
}
