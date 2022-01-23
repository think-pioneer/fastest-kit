package xyz.thinktest.fastestapi.common.json.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Date: 2020/10/23
 */
public class JsonParse {
    private static final Configuration config = Configuration.defaultConfiguration()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider());

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
