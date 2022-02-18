package xyz.thinktest.fastestapi.core.enhance.joinpoint.method;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.thinktest.fastestapi.core.annotations.Autowired;
import xyz.thinktest.fastestapi.core.annotations.Component;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Listeners(TestRunListener.class)
@Test
@Component
public class MethodProcessableTest {
    @Autowired
    Biz biz;

    public void customAnnTest(){
        Num num = new Num(0);
        num.add(1);
        biz.biz(num);
        Assert.assertEquals(3, num.getValue());
    }
}
