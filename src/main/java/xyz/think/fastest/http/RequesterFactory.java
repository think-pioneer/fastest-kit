package xyz.think.fastest.http;

import xyz.think.fastest.core.ApplicationBean;
import xyz.think.fastest.http.internal.HttpCacheInternal;
import xyz.think.fastest.http.metadata.Header;
import xyz.think.fastest.http.metadata.Headers;

import java.util.Arrays;
import java.util.Objects;

public final class RequesterFactory {
    private static final HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;
    private RequesterFactory(){}

    /**
     * 当作普通客户端使用，可以在每次使用时提供cookie等鉴权
     */
    public static Requester create(){
        Class<Requester> requesterType = httpCacheInternal.get("fastest.http.requester");
        return ApplicationBean.getOriginBean(requesterType);
    }

    /**
     * 提供鉴权信息，当作单独的客户端使用
     * @param auths 包含鉴权信息的header集合
     */
    public static Requester create(Headers auths){
        Requester requester = AuthManager.getRequester(auths);
        if(Objects.isNull(requester)) {
            Class<Requester> requesterType = httpCacheInternal.get("fastest.http.requester");
            requester = ApplicationBean.getOriginBean(requesterType, new Class[]{Headers.class}, new Object[]{auths});
            AuthManager.set(requester, auths);
        }
        return requester;
    }

    /**
     * 提供一个鉴权信息，只有一个键值对
     * @param auth 包含一对kv的鉴权信息
     */
    public static Requester create(Header auth){
        Requester requester = AuthManager.getRequester(Headers.newEmpty().write(auth));
        if(Objects.isNull(requester)) {
            Class<Requester> requesterType = httpCacheInternal.get("fastest.http.requester");
            requester = ApplicationBean.getOriginBean(requesterType, new Class[]{Header.class}, new Object[]{auth});
            AuthManager.set(requester, Headers.newEmpty().write(auth));
        }
        return requester;
    }

    /**
     * 提供多组鉴权信息
     * @param auths 包含多组鉴权信息的header
     */
    public static Requester create(Header... auths){
        Headers headers = Headers.newEmpty();
        headers.addAll(Arrays.asList(auths));
        return create(headers);
    }
}
