package xyz.think.fastest.http;

import xyz.think.fastest.http.metadata.HttpMethod;
import xyz.think.fastest.http.metadata.Restfuls;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.core.annotations.RestTemp;

/**
 * @author: aruba
 * @date: 2022-02-03
 * 用于测试requester相关的功能
 */
@Component
public interface Controller {

    @RestTemp(host = "http://myhttp", api = "/{id}", method = HttpMethod.GET, auto = false)
    void restTypeTestHasRestfulsParams(Requester requester, Restfuls restfuls);

    @RestTemp(host = "http://myhttp", api = "/{id}", method = HttpMethod.GET, auto = false)
    void restTypeTestNoRestfulsParams(Requester requester);
}
