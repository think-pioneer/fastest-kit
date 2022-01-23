package xyz.thinktest.fastestapi.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import xyz.thinktest.fastestapi.common.json.JSONFactory;
import xyz.thinktest.fastestapi.common.json.jsonpath.JsonParse;
import xyz.thinktest.fastestapi.http.internal.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2020/10/17
 */
public class Asserts<T> extends Assert {
    private final String jsonString;

    Asserts(String json){
        this.jsonString = json;
    }

    private T parse(String path, Predicate...conditions){
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
        assertFalse(null, targetPath);
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
        List<T> result = (List<T>) parse(targetPath, conditions);
        assertTrue(CollectionUtils.isEmpty(result), assertErrorInfo);
    }

    public void assertListEmpty(String targetPath, Predicate ...conditions){
        assertListEmpty(null, targetPath, conditions);
    }

    public void assertListNotEmpty(String assertErrorInfo, String targetPath, Predicate ...conditions){
        List<T> result = (List<T>) parse(targetPath, conditions);
        assertTrue(CollectionUtils.isNotEmpty(result), assertErrorInfo);
    }

    public void assertNotEmpty(String targetPath, Predicate ...conditions){
        assertListNotEmpty(null, targetPath, conditions);
    }

    public void assertListContains(String assertErrorInfo,Object exceptValue, String targetPath, Predicate ...conditions){
        List<T> result = (List<T>) parse(targetPath, conditions);
        assertTrue(result.contains(exceptValue), assertErrorInfo);
    }

    public void assertListContains(Object exceptValue, String targetPath, Predicate ...conditions){
        assertListContains(null, exceptValue, targetPath, conditions);
    }

    public void assertAllEqual(Object exceptValue, String targetPath, Predicate ...conditions){
        assertAllEqual(null, exceptValue, targetPath, conditions);
    }

    public void assertAllEqual(String assertErrorInfo, Object exceptValue, String targetPath, Predicate ...conditions){
        ConcurrentHashMap<String, Object> exceptMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();

        JsonNode exceptNode;
        if(exceptValue instanceof JsonNode){
            exceptNode = (JsonNode) exceptValue;
        } else {
            exceptNode = JSONFactory.stringToJson(exceptValue.toString());
        }
        JsonNode resultNode = JSONFactory.objectToJson(parse(targetPath, conditions));
        CompletableFuture<Void> exceptFuture = CompletableFuture.runAsync(() -> jsonNodeIter("", exceptNode, exceptMap));
        CompletableFuture<Void> resultFuture = CompletableFuture.runAsync(() -> jsonNodeIter("", resultNode, resultMap));
        CompletableFuture.allOf(exceptFuture, resultFuture).join();
        assertTrue(exceptMap.equals(resultMap), assertErrorInfo);
    }

    private void jsonNodeIter(String key, JsonNode node, Map<String, Object> kvMap) {
        if (node.isValueNode())
        {
            kvMap.put(key, node.toString());
            return;
        }

        if (node.isObject())
        {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext())
            {
                Map.Entry<String, JsonNode> entry = it.next();
                jsonNodeIter(key + "." +entry.getKey(), entry.getValue(), kvMap);
            }
        }

        if (node.isArray())
        {
            Iterator<JsonNode> it = node.iterator();
            int flag = 0;
            while (it.hasNext())
            {
                int arrayFlag = key.lastIndexOf("[");
                if(arrayFlag != -1){
                    key = key.substring(0, key.lastIndexOf("["));
                }
                key = key + "[" + flag + "]";
                jsonNodeIter(key, it.next(), kvMap);
                flag++;
            }
        }
    }
}
