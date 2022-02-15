package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.core.ApplicationBean;
import xyz.thinktest.fastestapi.http.internal.HttpCacheInternal;
import xyz.thinktest.fastestapi.http.metadata.Header;
import xyz.thinktest.fastestapi.http.metadata.Headers;

import java.util.Arrays;
import java.util.Objects;

public final class RequesterFactory {
    private static final HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;
    private RequesterFactory(){}

    public static Requester create(){
        Class<Requester> requesterType = httpCacheInternal.get("fastest.api.http.requester");
        return ApplicationBean.getOriginBean(requesterType);
    }

    public static Requester create(Headers auths){
        Requester requester = AuthManager.getRequester(auths);
        if(Objects.isNull(requester)) {
            Class<Requester> requesterType = httpCacheInternal.get("fastest.api.http.requester");
            requester = ApplicationBean.getOriginBean(requesterType, new Class[]{Headers.class}, new Object[]{auths});
            AuthManager.set(requester, auths);
        }
        return requester;
    }

    public static Requester create(Header auth){
        Requester requester = AuthManager.getRequester(Headers.newEmpty().write(auth));
        if(Objects.isNull(requester)) {
            Class<Requester> requesterType = httpCacheInternal.get("fastest.api.http.requester");
            requester = ApplicationBean.getOriginBean(requesterType, new Class[]{Header.class}, new Object[]{auth});
            AuthManager.set(requester, Headers.newEmpty().write(auth));
        }
        return requester;
    }

    public static Requester create(Header... auths){
        Headers headers = Headers.newEmpty();
        headers.addAll(Arrays.asList(auths));
        return create(headers);
    }
}
