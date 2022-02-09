package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.core.ApplicationBean;
import xyz.thinktest.fastestapi.http.metadata.Header;
import xyz.thinktest.fastestapi.http.metadata.Headers;

import java.util.HashMap;
import java.util.Map;

public final class RequesterFactory {
    private static final HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;
    private RequesterFactory(){}

    public static Requester create(){
        Class<Requester> requesterType = httpCacheInternal.get("fastest.api.http.requester");
        return ApplicationBean.getEnhanceBean(requesterType);
    }

    public static Requester create(Map<Object, Object> authentication){
        Class<Requester> requesterType = httpCacheInternal.get("fastest.api.http.requester");
        return ApplicationBean.getOriginBean(requesterType, new Class[]{Map.class}, new Object[]{authentication});
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
