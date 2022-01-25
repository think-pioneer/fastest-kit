package xyz.thinktest.fastestapi.common.json.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastestapi.common.json.JSONFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2020/10/23
 */
public class JsonParse {
    private static final Configuration config = Configuration.defaultConfiguration()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider());

    public static void jsonNodeIter(JsonNode json, Map<String, Object> map){
        jsonNodeIter("", json, map);
    }

    public static void jsonNodeIter(String json, Map<String, Object> map){
        jsonNodeIter("", JSONFactory.stringToJson(json), map);
    }

    private static void jsonNodeIter(String key, JsonNode node, Map<String, Object> kvMap) {
        if (node.isValueNode()) {
            key = key.startsWith(".") ? key.substring(1) : key;
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

    public static Read read(String json){
        return new Read(json);
    }

    public static Read read(JsonNode json){
        return new Read(json.toString());
    }

    public static class Read {
        String json;
        Read(String json){
            this.json = json;
        }

        public <T> T parse(String path, Class<T> clazz){
            path = StringUtils.isEmpty(path) ? "$" : path;
            return JsonPath.using(config).parse(json).read(path, clazz);
        }

        public <T> T parse(String path){
            path = StringUtils.isEmpty(path) ? "$" : path;
            return JsonPath.using(config).parse(json).read(path, new TypeRef<T>() {});
        }

        public <T> T parse(String path, List<Predicate> conditions){
            return parse(path, conditions.toArray(new Predicate[0]));
        }

        public <T> T parse(String path, Predicate ...conditions){
            path = StringUtils.isEmpty(path) ? "$" : path;
            int conditionsLen = conditions.length;
            Selector[] selectors = new Selector[conditionsLen];
            for(int i = 0; i < conditionsLen; i++){
                selectors[i] = Selector.selector(conditions[i]);
            }
            return JsonPath.read(json, path, selectors);
        }
    }
}
