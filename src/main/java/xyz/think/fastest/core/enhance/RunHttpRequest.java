package xyz.think.fastest.core.enhance;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import xyz.think.fastest.common.exceptions.EnhanceException;
import xyz.think.fastest.common.exceptions.HttpException;
import xyz.think.fastest.core.annotations.HttpLog;
import xyz.think.fastest.core.internal.enhance.methodhelper.AbstractMethodProcess;
import xyz.think.fastest.http.*;
import xyz.think.fastest.http.metadata.*;
import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.logger.FastestLoggerFactory;
import xyz.think.fastest.utils.string.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date: 2021/11/30
 */
public abstract class RunHttpRequest extends AbstractMethodProcess {
    private static final FastestLogger logger = FastestLoggerFactory.getLogger("HttpRequest");
    protected void run(Method method, Object[] args, String url, HttpMethod httpMethod, boolean isAuto, boolean isSync){
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
        if(Objects.isNull(requester)){
            throw new HttpException(StringUtils.format("{0}.{1}use auto send needs Requester1 object as params,but not found",method.getDeclaringClass().getName(), method.getName()));
        }
        Headers auth = AuthManager.get(requester);
        if(CollectionUtils.isNotEmpty(auth)){
            for(Meta header:auth){
                requester.metadata().setHeader((Header) header);
            }
        }
        if(isAuto){
            if(!isUrl(requester.metadata().getUrl().getUrl())){
                throw new EnhanceException(StringUtils.format("url:\"{0}\" is not a valid url", url));
            }
            HttpLog httpLog = method.getDeclaredAnnotation(HttpLog.class);
            boolean showRequestLog = Objects.isNull(httpLog) ? requester.settings().isShowRequestLog() : httpLog.showRequestLog();
            boolean showResponseLog = Objects.isNull(httpLog) ? requester.settings().isShowResponseLog() : httpLog.showResponseLog();
            Metadata metadata = requester.metadata();
            if(showRequestLog) {
                logger.info("**********HTTP REQUEST**********\n" +
                        "Http Url:{}\n" +
                        "Http Method:{}\n" +
                        "Http Header:{}\n" +
                        "Http QueryParameters:{}\n" +
                        "Http Forms:{}\n" +
                        "Http Json:{}", metadata.getUrl().getFullUrl(), metadata.getMethod().getMethodName(), metadata.getHeaders(), metadata.getParameters(), metadata.getForms(), metadata.getJson());
            }
//            if(isSync){
//                requester.sync();
//            } else {
//                requester.async();
//            }
            requester.settings().setIsSync(isSync);
            requester.send();
            if(showResponseLog) {
                Responder responder = requester.getResponder();
                if (Objects.isNull(responder)) {
                    logger.info("**********HTTP RESPONSE**********\n" +
                            "Http Status Code:null\n" +
                            "Http Response Header:null\n" +
                            "Http Response body:null");
                } else {
                    logger.info("**********HTTP RESPONSE**********\n" +
                            "Http Status Code:{}\n" +
                            "Http Response Header:{}\n" +
                            "Http Response body:{}", responder.stateCode(), responder.headers(), responder.bodyToString());
                }
            }
            this.sendAfterRecovery(requester.settings(), requester.metadata());
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

    /**
     * 请求结束后的清理工作
     */
    private void sendAfterRecovery(Settings settings, Metadata metadata){
        if(settings.isCleanMetadata()){
            metadata.recovery();
            return;
        }
        if(settings.isCleanBody()){
            metadata.headersRecovery().parametersRecovery().formRecovery().jsonRecovery();
        }
    }
}
