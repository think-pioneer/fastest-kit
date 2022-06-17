package xyz.thinktest.fastestapi.http;

import okhttp3.Response;

/**
 *
 * @Date: 2020/11/15
 * @Desc: http response object
 */

public class DefaultResponder extends AbstractDefaultResponder {

    public DefaultResponder(Response response) {
        super(response);
    }

    public DefaultResponder() {
        super();
    }
}
