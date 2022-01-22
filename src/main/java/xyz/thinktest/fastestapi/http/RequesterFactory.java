package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.http.DefaultRequester;
import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.http.metadata.Header;
import xyz.thinktest.fastestapi.http.metadata.Headers;

import java.util.HashMap;
import java.util.Map;

public final class RequesterFactory {
    private RequesterFactory(){}

    public static Requester create(){
        return new DefaultRequester();
    }

    public static Requester create(Map<Object, Object> authentication){
        return new DefaultRequester(authentication);
    }

    public static Requester create(Header header){
        Map<Object, Object> authentication = new HashMap<>();
        authentication.put(header.getKey(), header.getValue());
        return create(authentication);
    }

    public static Requester create(Headers headers){
        Map<Object, Object> authentication = new HashMap<>();
        headers.forEach((header) -> authentication.put(header.getKey(), String.valueOf(header.getValue())));
        return create(authentication);
    }
}
