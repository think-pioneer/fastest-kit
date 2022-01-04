package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastest.common.exceptions.EnhanceException;
import xyz.thinktest.fastest.common.exceptions.JsonException;
import xyz.thinktest.fastest.core.annotations.RestMetadata;
import xyz.thinktest.fastest.core.annotations.RestServer;
import xyz.thinktest.fastest.core.enhance.joinpoint.method.JoinPoint;
import xyz.thinktest.fastest.http.Metadata;
import xyz.thinktest.fastest.http.Requester;
import xyz.thinktest.fastest.http.metadata.HttpMethod;
import xyz.thinktest.fastest.http.metadata.HttpMethodBuilder;
import xyz.thinktest.fastest.core.rest.http.metadata.ReadApiConfig;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Date: 2021/11/28
 */
public class RestMetadataProcess<T> extends AbstractRestAnnotationProcess<T> {

    @Override
    public void process(JoinPoint<T> joinPoint){
        this.exec(joinPoint);
    }

    private void exec(JoinPoint<T> joinPoint){
        Method method = joinPoint.getMethod();
        RestMetadata restMetadata = (RestMetadata) joinPoint.getAnnotation();
        Object[] args = joinPoint.getArgs();
        boolean paramsValid = false;
        for(Object arg:args){
            if ((arg instanceof Requester) || (arg instanceof Metadata)) {
                paramsValid = true;
                break;
            }
        }
        if(args.length == 0 || !paramsValid){
            throw new EnhanceException(ObjectUtil.format("method:[{}] at least one parameter of Requester or Metadata is required"));
        }
        Class<?> clazz = method.getDeclaringClass();
        RestServer restServer = clazz.getAnnotation(RestServer.class);
        String serverName = restMetadata.serverName().trim();
        String file = restMetadata.file().trim();
        String apiName = restMetadata.apiName().trim();
        boolean isAuto = restMetadata.auto();
        boolean isSync = restMetadata.sync();
        if(Objects.nonNull(restServer) && "".equals(serverName)){
            serverName = restServer.value().trim();
        }
        if(Objects.nonNull(restServer) && "".equals(file)){
            file = restServer.file().trim();
        }
        file = StringUtils.isEmpty(file) ? null : file;
        if("".equals(serverName) || "".equals(apiName)){
            throw new EnhanceException("server name or api name can't empty");
        }
        ReadApiConfig.Uri uriObj = ReadApiConfig.getApi(serverName, apiName, file);
        if(Objects.isNull(uriObj)){
            if(Objects.nonNull(file)) {
                throw new JsonException(ObjectUtil.format("form api conf not fount {}.{} from {}", serverName, apiName, file));
            }
            throw new JsonException(ObjectUtil.format("form api conf not fount {}.{}", serverName, apiName));
        }
        String url = uriObj.getUrl();
        HttpMethod httpMethodType = HttpMethodBuilder.build(uriObj.getMethod().trim().toUpperCase());
        if(StringUtils.isEmpty(url)){
            throw new EnhanceException(ObjectUtil.format("Class={}, method={} url is empty", clazz.getName(), method.getName()));
        }
        buildMetadata(method, args, url, httpMethodType, isAuto, isSync);
    }
}
