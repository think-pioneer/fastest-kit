package xyz.thinktest.fastest.core.internal.enhance.methodhelper;

import xyz.thinktest.fastest.common.exceptions.EnhanceException;
import xyz.thinktest.fastest.common.exceptions.HttpException;
import xyz.thinktest.fastest.http.Metadata;
import xyz.thinktest.fastest.http.Requester;
import xyz.thinktest.fastest.http.metadata.HttpMethod;
import xyz.thinktest.fastest.http.metadata.Restfuls;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date: 2021/11/30
 */
public abstract class AbstractRestAnnotationProcess extends AbstractMethodProcess {
    protected void buildMetadata(Method method, Object[] args, String url, HttpMethod httpMethod, boolean isAuto, boolean isSync){
        List<Object> argList = new ArrayList<>(Arrays.asList(args));
        AtomicReference<String> newUrl = new AtomicReference<>();
        for(Object arg:args){
            if(arg instanceof Restfuls){
                Restfuls restfuls = (Restfuls) arg;
                Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
                Matcher matcher = pattern.matcher(url);
                Map<String, String> restParams = new HashMap<>();
                while (matcher.find()){
                    restParams.put(matcher.group(1), matcher.group());
                }
                if(restParams.isEmpty()){
                    throw new EnhanceException(ObjectUtil.format("url:[{}] not restful url", url));
                }
                String finalUrl = url;
                restfuls.forEach((key, value) -> newUrl.set(finalUrl.replace(restParams.get(key), String.valueOf(value.getValue()))));
                url = newUrl.get();
                break;
            }
        }
        if(!isUrl(url)){
            throw new EnhanceException(ObjectUtil.format("url:\"{}\" is not a valid url", url));
        }
        Requester requester = null;
        for(int i = 0; i < argList.size(); i++){
            Object arg = argList.get(i);
            if(arg instanceof Requester){
                requester = (Requester) arg;
                requester.metadata().setUrl(url).setHttpMethod(httpMethod);
                argList.set(i, requester);
                break;
            } else if(arg instanceof Metadata) {
                Metadata metadata = (Metadata) arg;
                metadata.setUrl(url).setHttpMethod(httpMethod);
                argList.set(i, metadata);
                break;
            }
        }
        if(isAuto){
            if(Objects.isNull(requester)){
                throw new HttpException(ObjectUtil.format("{}.{}use auto send needs Requester object as params,but not found",method.getDeclaringClass().getName(), method.getName()));
            }
            if(isSync){
                requester.sync();
            } else {
                requester.async();
            }
        }
    }

    /**
     * check url
     * @param url url
     * @return result
     */
    protected boolean isUrl(String url){
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\&%\\+\\$#_=]*)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url.trim());
        return matcher.matches();
    }
}
