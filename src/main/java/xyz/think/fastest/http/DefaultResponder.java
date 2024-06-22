package xyz.think.fastest.http;

import okhttp3.Response;
import xyz.think.fastest.core.annotations.Component;

/**
 *
 * @Date: 2020/11/15
 * @Desc: http response object
 */
@Component
public class DefaultResponder extends AbstractDefaultResponder {

    public DefaultResponder(Response response) {
        super(response);
    }

    public DefaultResponder() {
        super();
    }
}
