package xyz.think.fastest.http;

import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.http.metadata.Header;
import xyz.think.fastest.http.metadata.Headers;


/**
 *
 * @Date: 2020/11/15
 * @Desc: 请求者,可以理解为一个通用的请求对象(将鉴权信息直接放入body中，每次请求时带入)，也可以理解为不同的请求对象(使用Requester(Map<String, String> authentication)构造方法)
 */
@Component
public class DefaultRequester extends AbstractDefaultRequester {

    /**
     * 需要在header中指定鉴权方式
     */
    public DefaultRequester(){
        super();
    }

    /**
     * 提供一个header中的鉴权参数，则该实例对象只使用该鉴权访问
     * 建议使用这种方式，如果直接放入body中，一旦在切换鉴权信息出问题时，会出现请求结果和预期不一致的情况
     * @param authentication 鉴权map
     */
    public DefaultRequester(Headers authentication){
        super(authentication);
    }

    /**
     * @see #DefaultRequester(Headers)
     * @param headers 鉴权信息
     */
    public DefaultRequester(Header... headers){
        super(headers);
    }

    /**
     * @see #DefaultRequester(Headers)
     * @param header 鉴权信息
     */
    public DefaultRequester(Header header){
        super(header);
    }
}
