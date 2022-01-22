package xyz.thinktest.fastestapi.common.cache;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class CacheTest {
    /**
     * 测试缓存管理器在多线程下是否工作正常
     */
    public void cacheSignedTest(){
        Object value = CacheManager.getInstance().get("unit test key");
        Assert.assertNull(value);
        Thread thread1 = new Thread(() -> CacheManager.getInstance().put("unit test key", "unit test value"));
        Thread thread2 = new Thread(() -> {
            Object value1 = CacheManager.getInstance().get("unit test key");
            Assert.assertEquals(String.valueOf(value1), "unit test value");
        });
        thread1.run();
        thread2.run();
    }
}
