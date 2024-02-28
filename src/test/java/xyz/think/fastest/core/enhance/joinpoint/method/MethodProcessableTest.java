package xyz.think.fastest.core.enhance.joinpoint.method;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.think.fastest.core.annotations.Autowired;
import xyz.think.fastest.core.annotations.Component;

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

    public void customAnnTest1(){
        Num num = new Num(0);
        num.add(1);
        biz.biz1(num);
        Assert.assertEquals(3, num.getValue());
    }

    public void customAnnTest2(){
        Num num = new Num(0);
        num.add(1);
        biz.biz2(num);
        Assert.assertEquals(2, num.getValue());
    }
}
