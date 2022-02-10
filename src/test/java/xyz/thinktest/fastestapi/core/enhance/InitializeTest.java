package xyz.thinktest.fastestapi.core.enhance;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.thinktest.fastestapi.core.annotations.Component;

/**
 * @author: aruba
 * @date: 2022-02-10
 */
@Listeners(TestRunListener.class)
@Test
@Component
public class InitializeTest {

    /**
     * 添加两个初始化操作，每个初始化操作+1
     */
    public void case1(){
        Assert.assertEquals(MyObj.INSTANCE.getFlag(), 3);
    }
}
