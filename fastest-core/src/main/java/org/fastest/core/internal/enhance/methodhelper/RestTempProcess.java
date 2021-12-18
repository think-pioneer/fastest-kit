package org.fastest.core.internal.enhance.methodhelper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.fastest.common.exceptions.EnhanceException;
import org.fastest.common.json.JSONFactory;
import org.fastest.core.annotations.RestTemp;
import org.fastest.core.aspect.method.JoinPoint;
import org.fastest.core.internal.tool.AnnotationTool;
import org.fastest.http.metadata.HttpMethod;
import org.fastest.utils.ObjectUtil;
import org.fastest.utils.PropertyUtil;

import java.lang.reflect.Method;
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

        ObjectNode apiNode = JSONFactory.createObjectNode();
        Object[] args = joinPoint.getArgs();
        boolean isSave = restTemp.save();
        Boolean globalIsSave = (Boolean) PropertyUtil.get("rest.temp.save");;
        if(Objects.isNull(globalIsSave)){
            globalIsSave = isSave;
        }
        String api = restTemp.api().trim();
        String host = restTemp.host().trim();
        HttpMethod httpMethod = restTemp.method();
        boolean isAuto = restTemp.auto();
        boolean isSync = restTemp.sync();
        String url = host+api;
        if(args.length == 0){
            throw  new EnhanceException(ObjectUtil.format("method:[{}] haven't no parameters.", method.getName()));
        }
        buildMetadata(method, args, url, httpMethod, isAuto, isSync);
        if(globalIsSave){
            apiNode.put("apiName", restTemp.name());
            apiNode.put("host", host);
            apiNode.put("api", api);
            apiNode.put("method", httpMethod.getName());
            apiNode.put("desc", restTemp.desc());
        }
        RestTempWrite.add(apiNode);
    }
}
