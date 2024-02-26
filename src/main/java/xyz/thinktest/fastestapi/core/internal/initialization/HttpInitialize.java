package xyz.thinktest.fastestapi.core.internal.initialization;

import xyz.thinktest.fastestapi.common.exceptions.InitializationException;
import xyz.thinktest.fastestapi.http.DefaultRequester;
import xyz.thinktest.fastestapi.http.DefaultResponder;
import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.http.Responder;
import xyz.thinktest.fastestapi.http.internal.HttpCacheInternal;
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;
import xyz.thinktest.fastestapi.utils.reflects.ReflectUtil;
import xyz.thinktest.fastestapi.utils.string.StringUtils;

import java.util.Objects;

class HttpInitialize implements InitializeInternal {
    private static final HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;
    @Override
    public int order() {
        return InitOrderInternalManager.getInstance().consumer(HttpInitialize.class);
    }

    @Override
    public void executor() {
        try {
            String requesterClass = PropertyUtil.getProperty("fastest.api.http.requester");
            String responderClass = PropertyUtil.getProperty("fastest.api.http.responder");
            if(Objects.isNull(requesterClass)){
                requesterClass = DefaultRequester.class.getCanonicalName();
            }
            if (Objects.isNull(responderClass)) {
                responderClass = DefaultResponder.class.getCanonicalName();
            }
            Class<?> requester = Class.forName(requesterClass);
            Class<?> responder = Class.forName(responderClass);
            if(ReflectUtil.isSubclasses(requester, Requester.class)){
                throw new InitializationException(StringUtils.format("fastest.api.http.requester={0} is not Requester subclasses", requesterClass));
            }
            if(ReflectUtil.isSubclasses(responder, Responder.class)){
                throw new InitializationException(StringUtils.format("fastest.api.http.requester={0} is not Requester subclasses", requesterClass));
            }
            httpCacheInternal.set("fastest.api.http.requester", requester);
            httpCacheInternal.set("fastest.api.http.responder", responder);
        }catch (ClassNotFoundException e){
            throw new InitializationException("initialization http fail", e);
        }
    }
}
