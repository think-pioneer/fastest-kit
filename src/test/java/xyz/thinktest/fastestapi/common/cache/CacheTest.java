package xyz.thinktest.fastestapi.common.cache;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class CacheTest {
    /**
     * 测试缓存管理器在多线程下是否工作正常
     */
    public void cacheSignedTest(){
        Object value = CacheManager.get("unit test key");
        Assert.assertNull(value);
        Thread thread1 = new Thread(() -> CacheManager.put("unit test key", "unit test value"));
        Thread thread2 = new Thread(() -> {
            Object value1 = CacheManager.get("unit test key");
            Assert.assertEquals(String.valueOf(value1), "unit test value");
        });
        thread1.start();
        thread2.start();
    }

    /**
     * 测试缓存管理的定时清理功能
     * @throws InterruptedException
     */
    public void cacheTimerExpired() throws InterruptedException {
        CacheManager.put("cacheTimerExpiredKey", "cacheTimerExpiredValue", 5000);
        Thread.sleep(11000);
        Object value = Manager.CACHE_MANAGER.cacheMap.get("cacheTimerExpiredKey");
        Assert.assertNull(value);
    }
}
