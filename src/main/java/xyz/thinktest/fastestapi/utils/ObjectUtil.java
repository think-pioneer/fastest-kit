package xyz.thinktest.fastestapi.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date: 2020/10/25
 */
public final class ObjectUtil {
    private ObjectUtil(){}

    /**
     * format string。
     * usage: format(hello "{}", "world")
     * @param format origin string, contain placeholder
     * @param contents fill in the string of the placeholder。if not String, will call toString()
     * @return string
     */
    public static String format(String format, Object ...contents){
        List<Object> contentList = new ArrayList<>(Arrays.asList(contents));
        Pattern pattern = Pattern.compile("(\\{\\})+");
        Matcher matcher = pattern.matcher(format);
        String tmp = null;
        while (matcher.find()){
            String value = String.valueOf(contentList.get(0));
            if("{}".equals(value)){
                value = "{;SEAT;}";
            }
            tmp = matcher.replaceFirst(value);
            matcher = pattern.matcher(tmp);
            contentList.remove(0);
            if(CollectionUtils.isEmpty(contentList)){
                contentList.add("{;SEAT;}");
            }
        }
        if(Objects.nonNull(tmp)){
            tmp = tmp.replace("{;SEAT;}", "{}");
        }
        return tmp;
    }

    /**
     * Convert entity classes to Map
     * @param object entity
     * @return map
     */
    public static Map<?, ?> toMap(Object object){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(object);
            Map<?, ?> map = mapper.readValue(json, new TypeReference<Map<?, ?>>() {});
            return map;
        }catch (Exception e){
            throw new FastestBasicException("entity to map fail", e);
        }
    }

    /**
     *check map element type if key or value is Object type
     * @param obj map
     * @param keyType key type
     * @param valueType value type
     * @return result
     */
    public static boolean checkMapElementType(Object obj, Class<?> keyType, Class<?> valueType){
        Map<?, ?> map = (Map<?, ?>) obj;
        for(Entry<?,?> entry:map.entrySet()){
            if(ObjectUtils.notEqual(entry.getKey().getClass(), keyType) || ObjectUtils.notEqual(entry.getValue().getClass(), valueType)){
                return false;
            }
        }
        return true;
    }
}

