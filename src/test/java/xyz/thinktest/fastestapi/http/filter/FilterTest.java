package xyz.thinktest.fastestapi.http.filter;


import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.thinktest.fastestapi.core.annotations.Component;
import xyz.thinktest.fastestapi.http.Requester;
import xyz.thinktest.fastestapi.http.RequesterFactory;
import xyz.thinktest.fastestapi.http.metadata.HttpMethod;

@Listeners(TestRunListener.class)
@Test
@Component
public class FilterTest {

    public void filterTest(){
        Requester requester = RequesterFactory.create();
        requester.settings().setFilter(new FilterConfig(0, new Filter1())).setFilter(new FilterConfig(1, new Filter2()));
        requester.metadata().setUrl("http://www.baidu.com").setHttpMethod(HttpMethod.GET);
        requester.send();
    }
}
