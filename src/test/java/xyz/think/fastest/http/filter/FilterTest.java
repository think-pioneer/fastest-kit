package xyz.think.fastest.http.filter;


import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.think.fastest.http.RequesterFactory;
import xyz.think.fastest.http.metadata.HttpMethod;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.http.Requester;

@Listeners(TestRunListener.class)
@Test
@Component
public class FilterTest {

    public void filterTest(){
        Requester requester = RequesterFactory.create();
        requester.settings().setFilterConfigs(new FilterConfig(0, new Filter1())).setFilterConfigs(new FilterConfig(1, new Filter2()));
        requester.metadata().setUrl("http://www.baidu.com").setHttpMethod(HttpMethod.GET);
        Requester.send(requester);
    }
}
