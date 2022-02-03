package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import org.apache.commons.collections4.MapUtils;
import xyz.thinktest.fastestapi.http.Metadata;
import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.common.exceptions.HttpException;
import xyz.thinktest.fastestapi.http.metadata.HttpMethod;
import xyz.thinktest.fastestapi.http.metadata.Restfuls;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date: 2021/11/30
 */
public abstract class AbstractRestAnnotationProcess extends AbstractMethodProcess {
    protected void buildMetadata(Method method, Object[] args, String url, HttpMethod httpMethod, boolean isAuto, boolean isSync){
        List<Object> argList = new ArrayList<>(Arrays.asList(args));
        Restfuls restfuls = null;
        for(Object arg:args){
            if(arg instanceof Restfuls){
                restfuls = (Restfuls) arg;
                break;
            }
        }
        Requester requester = null;
        for(int i = 0; i < argList.size(); i++){
            Object arg = argList.get(i);
            if(arg instanceof Requester){
                requester = (Requester) arg;
                if(Objects.isNull(restfuls)) {
                    restfuls = requester.metadata().getRestfuls();
                }
                if(MapUtils.isNotEmpty(restfuls)){
                    requester.metadata().setUrl(restfuls.buildUrl(url)).setHttpMethod(httpMethod);
                }else {
                    requester.metadata().setUrl(url).setHttpMethod(httpMethod);
                }
                argList.set(i, requester);
                break;
            } else if(arg instanceof Metadata) {
                Metadata metadata = (Metadata) arg;
                restfuls = metadata.getRestfuls();
                if(MapUtils.isNotEmpty(restfuls)){
                    metadata.setUrl(restfuls.buildUrl(url)).setHttpMethod(httpMethod);
                }else{
                    metadata.setUrl(url).setHttpMethod(httpMethod);
                }
                argList.set(i, metadata);
                break;
            }
        }
        if(isAuto){
            if(Objects.isNull(requester)){
                throw new HttpException(ObjectUtil.format("{}.{}use auto send needs Requester object as params,but not found",method.getDeclaringClass().getName(), method.getName()));
            }
            if(!isUrl(requester.metadata().getUrl().string())){
                throw new EnhanceException(ObjectUtil.format("url:\"{}\" is not a valid url", url));
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
    private boolean isUrl(String url){
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\&%\\+\\$#_=]*)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url.trim());
        return matcher.matches();
    }
}
