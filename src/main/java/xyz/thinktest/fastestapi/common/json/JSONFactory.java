package xyz.thinktest.fastestapi.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import xyz.thinktest.fastestapi.common.exceptions.JsonException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @Date: 2020/10/16
 */
public class JSONFactory {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    private JSONFactory(){}

    public static JsonNode read(InputStream stream){
        try {
            return mapper.readTree(stream);
        }catch (IOException e){
            throw new JsonException("stream to json node fail", e);
        }
    }

    /**
     * 创建object json对象
     * @return ObjectNode对象
     */
    public static ObjectNode createObjectNode(){
        return JsonNodeFactory.instance.objectNode();
    }

    /**
     * 创建array node对象
     * @return ArrayNode对象
     */
    public static ArrayNode createArrayNode(){
        return JsonNodeFactory.instance.arrayNode();
    }

    /**
     * 通过json字符串创建json node
     * @param jsonString json字符串
     * @return json node
     */
    public static JsonNode stringToJson(String jsonString){
        try {
            return mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new JsonException("string to json node fail:(string="+ jsonString +")");
        }
    }

    /**
     * json字符串转java对象
     * @param jsonString json字符串
     * @param clazz 对象类
     * @return object
     */
    public static <T> T stringToObject(String jsonString, Class<T> clazz){
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonException("string to object fail:(string=" + jsonString + ",class=" + clazz.getName() + ")", e);
        }
    }

    /**
     * 字符串转对象
     * @param jsonString json字符串
     * @param typeReference 反省类型
     * @param <T> 对象的类型
     * @return Java对象
     */
    public static <T> T stringToObject(String jsonString, TypeReference<T> typeReference){
        try {
            return mapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            throw new JsonException("string to object fail:(string="+ jsonString + "typeReference=" + typeReference.getType() + ")", e);
        }
    }

    /**
     * string转换为object
     * @param jsonString json string
     * @param collectionClass Collection class
     * @param elementClasses Collection element class
     * @param <T> 对象类型
     * @return Java对象
     */
    public static <T> T stringToObject(String jsonString, Class<?> collectionClass, Class<?> ...elementClasses){
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return mapper.readValue(jsonString, javaType);
        } catch (JsonProcessingException e) {
            throw new JsonException("string to object fail:(json=" + jsonString + ",collectionType=" + collectionClass.getName() + ",elementType=" + Arrays.toString(elementClasses), e);
        }
    }

    /**
     * object转换为JsonNode
     * @param object 带转换对象
     * @param <T> 带转换对象的类型
     * @return JsonNode
     */
    public static <T> JsonNode objectToJson(T object){
        try {
            return JSONFactory.stringToJson(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new JsonException("object to json fail:(object="+ object.getClass().getName() +")", e);
        }
    }

    /**
     * 对象转json字符串
     * @param object Java对象
     * @param <T> 泛型对象
     * @return 对象的json字符串
     */
    public static <T> String objectToString(T object){
        try{
            return mapper.writeValueAsString(object);
        }catch (JsonProcessingException e) {
            throw new JsonException("object to json  string fail:(object="+ object.getClass().getName() +")", e);
        }
    }

    /**
     * 格式化json字符串
     * @param jsonString json字符串
     * @return 美化后的json字符串
     */
    public static String jsonPretty(String jsonString){
        try {
            return mapper.readTree(jsonString).toPrettyString();
        } catch (JsonProcessingException e) {
            throw new JsonException("format json string fail:(string=" + jsonString + ")", e);
        }
    }
}
