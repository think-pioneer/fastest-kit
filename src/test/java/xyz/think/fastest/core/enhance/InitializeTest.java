package xyz.think.fastest.core.enhance;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.listener.TestRunListener;
import xyz.think.fastest.core.annotations.Component;
import xyz.think.fastest.http.ApiConfigTemplate;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
    public void case1() throws FileNotFoundException {
        Assert.assertEquals(MyObj.INSTANCE.getFlag(), 3);
        FileOutputStream stream = new FileOutputStream(FileDescriptor.out);
        ApiConfigTemplate.printTemplate(stream);
        System.out.println(stream.toString());
    }
}
