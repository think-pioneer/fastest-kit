package xyz.think.fastest.core.internal.enhance.methodhelper;

import xyz.think.fastest.common.exceptions.EnhanceException;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.annotations.Pointcut;
import xyz.think.fastest.core.annotations.RestTemp;
import xyz.think.fastest.core.enhance.RunHttpRequest;
import xyz.think.fastest.core.enhance.joinpoint.method.JoinPoint;
import xyz.think.fastest.http.ReadApiConfig;
import xyz.think.fastest.http.metadata.HttpMethod;
import xyz.think.fastest.utils.files.PropertyUtil;
import xyz.think.fastest.utils.string.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @Date: 2021/11/28
 */
@Component
@Pointcut(annotation = RestTemp.class, before = true)
public class RestTempProcess extends RunHttpRequest {

    @Override
    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        Method method = joinPoint.getMethod();
        RestTemp restTemp = (RestTemp) joinPoint.getAnnotation();

        Object[] args = joinPoint.getArgs();
        boolean isSave = restTemp.save();
        Boolean globalIsSave = PropertyUtil.getOrDefault("fastest.rest.temp.save", false);
        String api = restTemp.api().trim();
        String host = restTemp.host().trim();
        HttpMethod httpMethodType = restTemp.method();
        boolean isAuto = restTemp.auto();
        boolean isSync = restTemp.sync();
        String url = host+api;
        if(args.length == 0){
            throw  new EnhanceException(StringUtils.format("method:[{0}] haven't no parameters.", method.getName()));
        }
        run(method, args, url, httpMethodType, isAuto, isSync);
        //全局参数false且单次参数为false，不保存.其余全局参数和单次参数任一一个为true都要保存。
        if (!globalIsSave && !isSave) {
            return;
        }
        ReadApiConfig.Server server = ReadApiConfig.Server.init();
        server.setHost(host);
        ReadApiConfig.Uri uri = ReadApiConfig.Uri.init();
        uri.setUri(api);
        uri.setMethod(httpMethodType.getMethodName());
        uri.setUriName(restTemp.name());
        uri.setDesc(restTemp.desc());
        uri.setHost(host);
        uri.setUrl(url);
        server.setUris(new ArrayList<ReadApiConfig.Uri>(){{add(uri);}});
        RestTempCache.add(server);
    }
}
