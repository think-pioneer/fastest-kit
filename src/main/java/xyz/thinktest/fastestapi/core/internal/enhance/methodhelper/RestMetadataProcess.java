package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.enhance.RunHttpRequest;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.method.JoinPoint;
import xyz.thinktest.fastestapi.core.rest.http.metadata.ReadApiConfig;
import xyz.thinktest.fastestapi.http.Metadata;
import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.common.exceptions.JsonException;
import xyz.thinktest.fastestapi.core.annotations.RestMetadata;
import xyz.thinktest.fastestapi.core.annotations.RestServer;
import xyz.thinktest.fastestapi.http.metadata.HttpMethod;
import xyz.thinktest.fastestapi.http.metadata.HttpMethodBuilder;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Date: 2021/11/28
 */
@Pointcut(annotation = RestMetadata.class)
public class RestMetadataProcess extends RunHttpRequest {

    @Override
    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
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
            throw new EnhanceException(ObjectUtil.format("method:[{}] at least one parameter of Requester1 or Metadata is required"));
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
        run(method, args, url, httpMethodType, isAuto, isSync);
    }
}
