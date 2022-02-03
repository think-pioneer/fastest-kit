package xyz.thinktest.fastestapi.http;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.thinktest.fastestapi.core.annotations.Autowired;
import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.http.metadata.Restfuls;

/**
 * @author: aruba
 * @date: 2022-02-03
 */
@Listeners(TestRunListener.class)
@Test
@Component
public class RequesterTest {
    @Autowired
    Controller controller;

    /**
     * 测试restful的用例
     * 在方法参数中包含restful参数
     */
    public void restTypeTestHasRestfulsParams(){
        Requester requester = RequesterFactory.create();
        Restfuls restfuls = Restfuls.newEmpty();
        restfuls.write("id", "110");
        controller.restTypeTestHasRestfulsParams(requester, restfuls);
        Assert.assertEquals(requester.metadata().getUrl().toString(), "http://myhttp/110");
    }

    /**
     * 测试restful的用例
     * 在metadata中包含restful
     */
    public void restTypeTestNoRestfulsParams(){
        Requester requester = RequesterFactory.create();
        Restfuls restfuls = Restfuls.newEmpty();
        restfuls.write("id", "110");
        requester.metadata().setRestfuls(restfuls);
        controller.restTypeTestNoRestfulsParams(requester);
        Assert.assertEquals(requester.metadata().getUrl().toString(), "http://myhttp/110");
    }
}
