package xyz.thinktest.fastestapi.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;
import xyz.thinktest.fastestapi.utils.string.StringUtils;

import java.io.*;
import java.util.Map;
import java.util.Map.Entry;

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
     * 建议使用StringUtils.format
     * @see StringUtils#format(String, Object...)
     */
    @Deprecated
    public static String format(String format, Object ...contents){
        return StringUtils.format2(format, contents);
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
            return mapper.readValue(json, new TypeReference<Map<?, ?>>() {});
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

    /**
     * 如果src为null，则返回defaultvalue
     */
    public static <T> T nullOfDefault(T src, T defaultValue){
        if(null == src){
            return defaultValue;
        }
        return src;
    }

    /**
     * 深拷贝
     */
    public static <T> T deepCopy(T src){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(src);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            T deepObj = (T) ois.readObject();
            ois.close();
            bais.close();
            oos.close();
            baos.close();
            return deepObj;
        }catch (IOException | ClassNotFoundException e){
            throw new FastestBasicException("deep copy object fail", e);
        }
    }
}

