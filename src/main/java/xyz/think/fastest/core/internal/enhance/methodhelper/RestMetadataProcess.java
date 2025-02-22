package xyz.think.fastest.core.internal.enhance.methodhelper;

import xyz.think.fastest.common.exceptions.EnhanceException;
import xyz.think.fastest.common.exceptions.JsonException;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.annotations.Pointcut;
import xyz.think.fastest.core.annotations.RestMetadata;
import xyz.think.fastest.core.annotations.RestServer;
import xyz.think.fastest.core.enhance.RunHttpRequest;
import xyz.think.fastest.core.enhance.joinpoint.method.JoinPoint;
import xyz.think.fastest.http.Metadata;
import xyz.think.fastest.http.ReadApiConfig;
import xyz.think.fastest.http.Requester;
import xyz.think.fastest.http.metadata.HttpMethod;
import xyz.think.fastest.http.metadata.HttpMethodBuilder;
import xyz.think.fastest.utils.string.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Date: 2021/11/28
 */
@Component
@Pointcut(annotation = RestMetadata.class, before = true)
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
            throw new EnhanceException(StringUtils.format("method:[{0}] at least one parameter of Requester1 or Metadata is required"));
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
                throw new JsonException(StringUtils.format("form api conf not fount {0}.{1} from {2}", serverName, apiName, file));
            }
            throw new JsonException(StringUtils.format("form api conf not fount {0}.{1}", serverName, apiName));
        }
        String url = uriObj.getUrl();
        HttpMethod httpMethodType = HttpMethodBuilder.build(uriObj.getMethod().trim().toUpperCase());
        if(StringUtils.isEmpty(url)){
            throw new EnhanceException(StringUtils.format("Class={0}, method={1} url is empty", clazz.getName(), method.getName()));
        }
        run(method, args, url, httpMethodType, isAuto, isSync);
    }
}
