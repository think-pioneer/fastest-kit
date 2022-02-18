package xyz.thinktest.fastestapi.http.metadata;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author: aruba
 * @date: 2022-02-18
 */
@Test
public class MetadataTest {

    /**
     * 测试metamap写入空值
     */
    public void writeNull(){
        Parameters parameters = Parameters.newEmpty();
        parameters.writeAll(null, null);
        Assert.assertEquals(parameters.size(), 0);
        parameters.write("1", null);
        Assert.assertEquals(parameters.size(), 1);

    }
}
