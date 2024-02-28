package xyz.think.fastest.http.metadata;

import xyz.think.fastest.common.exceptions.HttpException;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2021/12/24
 */
public class HttpMethodBuilder {
    private static volatile HttpMethodBuilder instance = null;
    private final ConcurrentHashMap<String, HttpMethod> map = new ConcurrentHashMap<>();

    public static HttpMethodBuilder getInstance(){
        if(Objects.isNull(HttpMethodBuilder.instance)){
            synchronized (HttpMethodBuilder.class){
                if(Objects.isNull(HttpMethodBuilder.instance)){
                    instance = new HttpMethodBuilder();
                }
            }
        }
        return instance;
    }

    /**
     * build http method executor
     * @param name http method name
     */
    public static HttpMethod build(String name){
        HttpMethodBuilder builder = getInstance();
        HttpMethod method = builder.map.get(name);
        if(Objects.isNull(method)){
            method = Arrays.stream(HttpMethod.values()).filter(httpMethod -> httpMethod.getMethodName().equals(name)).findFirst().orElseThrow(() -> new HttpException("Bad HTTP method:"+name));
            builder.map.put(name, method);
        }
        return method;
    }
}
