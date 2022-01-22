package xyz.thinktest.fastestapi.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Test
public class JSONFactoryTest {

    public void string2Map(){
        String json = "{\"name\":\"json\"}";
        Map<String, String> map = JSONFactory.stringToObject(json, new TypeReference<Map<String, String>>() {});
        Assert.assertTrue(map.containsKey("name"));
        Assert.assertEquals(map.get("name"), "json");
    }

    public void map2String(){
        Map<String, String> map = new HashMap<String, String>(){{put("name", "json");}};
        String json = JSONFactory.objectToString(map);
        Assert.assertEquals(json, "{\"name\":\"json\"}");
    }
}
