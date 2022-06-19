package xyz.thinktest.fastestapi.http;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.thinktest.fastestapi.core.annotations.Autowired;
import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.http.metadata.Header;
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

    public void requesterCache(){
        Requester requester1 = RequesterFactory.create(new Header("Cookie", "fastest"));
        Requester requester2 = RequesterFactory.create(new Header("Cookie", "fastest"));
        Assert.assertEquals(requester1, requester2);

        Requester requester3 = RequesterFactory.create(new Header("Cookie", "fastest1"));
        Requester requester4 = RequesterFactory.create(new Header("Cookie", "fastest2"));
        Assert.assertNotEquals(requester3, requester4);
    }

    public void requestersCache(){
        Header header1 = new Header("Cookie1", "fastest1");
        Header header2 = new Header("Cookie2", "fastest2");
        Requester requester1 = RequesterFactory.create(header1, header2);
        Requester requester2 = RequesterFactory.create(header1, header2);
        Assert.assertEquals(requester1, requester2);

        Header header3 = new Header("Cookie1", "fastest1");
        Requester requester3 = RequesterFactory.create(header1, header3);
        Requester requester4 = RequesterFactory.create(header1, header2);
        Assert.assertNotEquals(requester3, requester4);
    }

//     不适合单侧
//    public void filter(){
//        Requester requester = RequesterFactory.create();
//        requester.settings(Settings.create()
//                .setFilter(new FilterConfig(1, new Filter1()))
//                .setFilter(new FilterConfig(2, new Filter2())))
//                .metadata(Metadata.create()
//                        .setUrl("http://localhost:8080/hello")
//                        .setHttpMethod(HttpMethod.GET)
//                        .setParameter("id", 1)
//                        .setParameter("id", 2)
//                );
//        requester.send().asserts().assertEqual(3, "id[2]");
//    }
}
