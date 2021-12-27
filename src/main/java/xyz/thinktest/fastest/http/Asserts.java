package xyz.thinktest.fastest.http;

import com.jayway.jsonpath.Predicate;
import net.minidev.json.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import xyz.thinktest.fastest.common.json.jsonpath.JsonParse;
import xyz.thinktest.fastest.http.internal.Assert;

/**
 * @Date: 2020/10/17
 */
public class Asserts extends Assert {
    private final String jsonString;

    Asserts(String json){
        this.jsonString = json;
    }

    private JSONArray parse(String path, Predicate...conditions){
        return JsonParse.read(this.jsonString).parse(path, conditions);
    }

    private Object parse(String path){
        return JsonParse.read(this.jsonString).parse(path);
    }

    public void assertTrue(String targetPath){
        assertTrue(null, targetPath);
    }

    public void assertTrue(String assertErrorInfo, String targetPath){
        Object result = parse(targetPath);
        if(result instanceof Boolean){
            assertTrue((Boolean) result);
        }
        fail(assertErrorInfo);
    }

    public void assertFalse(String targetPath){
        assertFalse(targetPath);
    }

    public void assertFalse(String assertErrorInfo, String targetPath) {
        Object result = parse(targetPath);
        if (result instanceof Boolean){
            assertFalse((Boolean) result);
        }
        fail(assertErrorInfo);
    }

    public void assertEqual(Object exceptValue, String targetPath){
        assertEqual(null, exceptValue, targetPath);
    }

    public void assertEqual(String assertErrorInfo, Object exceptValue, String targetPath){
        assertEquals(parse(targetPath), exceptValue, assertErrorInfo);
    }

    public void assertNotEqual(String assertErrorInfo, Object exceptValue, String targetPath){
        assertNotEquals(parse(targetPath), exceptValue, assertErrorInfo);
    }

    public void assertNotEqual(Object exceptValue, String targetPath){
        assertNotEqual(null, exceptValue, targetPath);
    }

    public void assertNull(String targetPath){
        assertNull(null, targetPath);
    }

    public void assertNull(String assertErrorInfo, String targetPath){
        assertNull(parse(targetPath), assertErrorInfo);
    }

    public void assertNotNull(String targetPath){
        assertNull(null, targetPath);
    }

    public void assertNotNull(String assertErrorInfo, String targetPath){
        assertNotNull(parse(targetPath), assertErrorInfo);
    }

    public void assertListEmpty(String assertErrorInfo, String targetPath, Predicate ...conditions){
        JSONArray result = parse(targetPath, conditions);
        assertTrue(CollectionUtils.isEmpty(result), assertErrorInfo);
    }

    public void assertListEmpty(String targetPath, Predicate ...conditions){
        assertListEmpty(null, targetPath, conditions);
    }

    public void assertListNotEmpty(String assertErrorInfo, String targetPath, Predicate ...conditions){
        JSONArray result = parse(targetPath, conditions);
        assertTrue(CollectionUtils.isNotEmpty(result), assertErrorInfo);
    }

    public void assertNotEmpty(String targetPath, Predicate ...conditions){
        assertListNotEmpty(null, targetPath, conditions);
    }

    public void assertListContains(String assertErrorInfo,Object exceptValue, String targetPath, Predicate ...conditions){
        JSONArray result = parse(targetPath, conditions);
        assertTrue(result.contains(exceptValue), assertErrorInfo);
    }

    public void assertListContains(Object exceptValue, String targetPath, Predicate ...conditions){
        assertListContains(null, exceptValue, targetPath, conditions);
    }
}
