package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.common.exceptions.EnhanceException;
import xyz.thinktest.fastest.core.annotations.RestTemp;
import xyz.thinktest.fastest.core.cnhance.method.JoinPoint;
import xyz.thinktest.fastest.core.rest.http.metadata.ReadApiConfig;
import xyz.thinktest.fastest.http.metadata.HttpMethod;
import xyz.thinktest.fastest.utils.ObjectUtil;
import xyz.thinktest.fastest.utils.files.PropertyUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @Date: 2021/11/28
 */
public class RestTempProcess extends AbstractRestAnnotationProcess {

    @Override
    public void process(JoinPoint joinPoint){
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        Method method = joinPoint.getMethod();
        RestTemp restTemp = (RestTemp) joinPoint.getAnnotation();

        Object[] args = joinPoint.getArgs();
        boolean isSave = restTemp.save();
        Boolean globalIsSave = (Boolean) PropertyUtil.get("rest.temp.save");;
        if(Objects.isNull(globalIsSave)){
            globalIsSave = isSave;
        }
        String api = restTemp.api().trim();
        String host = restTemp.host().trim();
        HttpMethod httpMethodType = restTemp.method();
        boolean isAuto = restTemp.auto();
        boolean isSync = restTemp.sync();
        String url = host+api;
        if(args.length == 0){
            throw  new EnhanceException(ObjectUtil.format("method:[{}] haven't no parameters.", method.getName()));
        }
        buildMetadata(method, args, url, httpMethodType, isAuto, isSync);
        ReadApiConfig.Server server = ReadApiConfig.Server.init();
        server.setHost(host);
        if(globalIsSave){
            ReadApiConfig.Uri uri = ReadApiConfig.Uri.init();
            uri.setUri(api);
            uri.setMethod(httpMethodType.getMethodName());
            uri.setUriName(restTemp.name());
            uri.setDesc(restTemp.desc());
            uri.setHost(host);
            uri.setUrl(url);
            server.setUris(new ArrayList<ReadApiConfig.Uri>(){{add(uri);}});
        }
        RestTempWrite.add(server);
    }
}
