package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.core.annotations.RestTemp;
import xyz.thinktest.fastestapi.http.metadata.HttpMethod;
import xyz.thinktest.fastestapi.http.metadata.Restfuls;

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
