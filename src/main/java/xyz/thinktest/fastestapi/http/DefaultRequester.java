package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.http.metadata.Header;
import xyz.thinktest.fastestapi.http.metadata.Headers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Date: 2020/11/15
 * @Desc: 请求者,可以理解为一个通用的请求对象(将鉴权信息直接放入body中，每次请求时带入)，也可以理解为不同的请求对象(使用Requester(Map<String, String> authentication)构造方法)
 */

final class DefaultRequester extends AbstractDefaultRequester {

    /**
     * 需要在body中指定鉴权方式
     */
    public DefaultRequester(){
        super(new HashMap<>());
    }

    /**
     * 提供一个header中的鉴权参数，则该实例对象只使用该鉴权访问
     * 建议使用这种方式，如果直接放入body中，一旦在切换鉴权信息出问题时，会出现请求结果和预期不一致的情况
     * @param authentication 鉴权map
     */
    public DefaultRequester(Map<Object, Object> authentication){
        super(authentication);
    }
}
