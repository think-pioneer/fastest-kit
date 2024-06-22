package xyz.think.fastest.core.internal.initialization;

import xyz.think.fastest.common.exceptions.InitializationException;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.internal.tool.AnnotationTool;
import xyz.think.fastest.http.DefaultRequester;
import xyz.think.fastest.http.DefaultResponder;
import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.Responder;
import xyz.think.fastest.http.internal.HttpCacheInternal;
import xyz.think.fastest.utils.files.PropertyUtil;
import xyz.think.fastest.utils.reflects.ReflectUtil;
import xyz.think.fastest.utils.string.StringUtils;

import java.util.Objects;

@Component
class HttpInitialize implements InitializeInternal {
    private static final HttpCacheInternal httpCacheInternal = HttpCacheInternal.INSTANCE;
    @Override
    public int order() {
        return InitOrderInternalManager.getInstance().consumer(HttpInitialize.class);
    }

    @Override
    public void executor() {
        try {
            String requesterClass = PropertyUtil.getProperty("fastest.http.requester");
            String responderClass = PropertyUtil.getProperty("fastest.http.responder");
            if(Objects.isNull(requesterClass)){
                requesterClass = DefaultRequester.class.getCanonicalName();
            }
            if (Objects.isNull(responderClass)) {
                responderClass = DefaultResponder.class.getCanonicalName();
            }
            Class<?> requester = Class.forName(requesterClass);
            Class<?> responder = Class.forName(responderClass);
            if(ReflectUtil.isSubclasses(requester, Requester.class)){
                throw new InitializationException(StringUtils.format("fastest.http.requester={0} is not Requester subclasses", requesterClass));
            }
            if(ReflectUtil.isSubclasses(responder, Responder.class)){
                throw new InitializationException(StringUtils.format("fastest.http.responder={0} is not Responder subclasses", requesterClass));
            }
            if (AnnotationTool.hasComponentAnnotation(requester)) {
                httpCacheInternal.set("fastest.http.requester", requester);
            }
            if (AnnotationTool.hasComponentAnnotation(responder)) {
                httpCacheInternal.set("fastest.http.responder", responder);
            }
        }catch (ClassNotFoundException e){
            throw new InitializationException("initialization http fail", e);
        }
    }
}
