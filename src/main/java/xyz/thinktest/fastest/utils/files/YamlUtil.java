package xyz.thinktest.fastest.utils.files;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastest.common.json.JSONFactory;
import xyz.thinktest.fastest.utils.files.iostream.InputStreamOptional;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.ComposerException;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2021/12/7
 */
@SuppressWarnings("unchecked")
public final class YamlUtil<T> {
    private static volatile YamlUtil instance = null;
    private final List<File> fileList;
    private final ConcurrentHashMap<String, ByteArrayOutputStream> cacheMap;

    private YamlUtil(){
        this.fileList = new ArrayList<>();
        this.cacheMap = new ConcurrentHashMap<>();
        FileUtil.collect(FileUtil.getClassPath(), fileList, new String[]{".yml", ".yaml"});
    }

    private static <T> YamlUtil<T> getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (YamlUtil.class) {
                if (Objects.isNull(instance)) {
                    instance = new YamlUtil<T>();
                }
            }
        }
        return instance;
    }

    /**
     * Get value by file name and key
     * @param path file name(path)
     * @param key value key
     */
    private Object getInternal(String path, String key){
        String[] keys = StringUtils.isEmpty(key) ? new String[]{} : key.split("\\.");
        if(Objects.nonNull(path)) {
            path = path.replace("/", File.separator).replace("\\", File.separator);
        }
        for (File file : fileList) {
            String filePath = file.getAbsolutePath();
            ByteArrayOutputStream baost = cacheMap.get(filePath);
            InputStream is;
            if (Objects.isNull(baost)) {
                is = FileUtil.read(filePath);
                baost = InputStreamOptional.ofNullable(is).ifPresent().get();
                cacheMap.put(filePath, baost);
            }
            Map<?, ?> map;
            try {
                map = new Yaml(new Constructor(HashMap.class)).load(new ByteArrayInputStream(baost.toByteArray()));
            }catch (ComposerException e){
                continue;
            }
            if(Objects.isNull(map)){
                continue;
            }
            HashMap<Object, Object> tmpMap = new HashMap<>(map);
            //没有key直接返回yaml对象
            if (keys.length == 0) {
                return map;
            }
            Object obj = map.get(keys[0]);
            //如果只有一层key并且该key有值，则视为找到了值，返回值
            if (keys.length == 1 && Objects.nonNull(obj)) {
                return obj;
            } else {
                //如果只有一个key，但是没有找到值，则清空tmpMap，不然会影响后面的跳过判断
                if (keys.length == 1) {
                    tmpMap.clear();
                }
            }
            for (int i = 0; i < keys.length - 1; i++) {
                obj = tmpMap.get(keys[i]);
                //如果该key的值为null，则视为在该文件中没有对应的值，直接break进行下一个文件的操作，并清空tempMap
                if (Objects.isNull(obj)) {
                    tmpMap.clear();
                    break;
                }
                //如果key有值，则重新赋值给tmpMap，进入下一轮key查找
                tmpMap = new HashMap<>((Map<?, ?>) obj);
            }
            //如果有文件路径则判断本次文件是不是期望的文件
            //如果是期望路径或者map不为空，则返回最终值
            if ((Objects.nonNull(path) && file.getAbsolutePath().endsWith(path)) || MapUtils.isNotEmpty(tmpMap)) {
                return tmpMap.get(keys[keys.length - 1]);
            }
        }

        return null;
    }

    /**
     * Traverse all YML and yaml until a qualified file is found and the value is taken
     * @param keys key-path
     */
    private Object getInternal(String keys){
        return getInternal(null, keys);
    }

    public List<Object> getAllInternal(String path, String key){
        String[] keys = StringUtils.isEmpty(key) ? new String[]{} : key.split("\\.");
        path = path.replace("/", File.separator).replace("\\", File.separator);
        ByteArrayOutputStream baost = cacheMap.get(path);
        InputStream is;
        if(Objects.isNull(baost)){
            is = FileUtil.read(path);
            baost = InputStreamOptional.ofNullable(is).ifPresent().get();
            cacheMap.put(path, baost);
        }
        Iterable<Object> iterables = new Yaml(new Constructor(HashMap.class)).loadAll(new ByteArrayInputStream(baost.toByteArray()));
        if(keys.length == 0){
            return (List<Object>) iterables;
        }
        if(keys.length == 1){
            List<Object> tmpList = new ArrayList<>();
            for(Object object:iterables){
                Map<?,?> map = (Map<?, ?>) object;
                tmpList.add(map.get(keys[0]));
            }
            return tmpList;
        }
        List<Object> tmpList = new ArrayList<>();
        for(Object object:iterables){
            Map<?,?> map = (Map<?, ?>) object;
            for(int i = 0; i < keys.length-1; i++){
                Object obj =  map.get(keys[i]);
                if(Objects.isNull(obj)){
                    break;
                }
                map = new HashMap<>((Map<?, ?>) obj);
            }
            tmpList.add(map);
        }
        return tmpList;
    }

    /**
     * to java  entity object
     */

    private T toEntityInternal(String path, Constructor constructor){
        path = path.replace("/", File.separator).replace("\\", File.separator);
        ByteArrayOutputStream baos = cacheMap.get(path);
        if(Objects.isNull(baos)){
            baos = InputStreamOptional.ofNullable(FileUtil.read(path)).ifPresent().get();
            cacheMap.put(path, baos);
        }
        Yaml yaml = new Yaml(constructor);
        return yaml.load(new ByteArrayInputStream(baos.toByteArray()));
    }

    private String getStringInternal(String path, String keys){
        Object value = getInternal(path, keys);
        return Objects.isNull(value) ? null : JSONFactory.objectToJson(value).asText();
    }

    private String getStringInternal(String keys){
        Object value = getInternal(keys);
        return Objects.isNull(value) ? null : JSONFactory.objectToJson(value).asText();
    }

    private Object getOrDefaultInternal(String path, String keys, Object defaultValue){
        Object obj = getInternal(path, keys);
        return Objects.nonNull(obj) ? obj:defaultValue;
    }

    private Object getOrDefaultInternal(String keys, Object defaultValue){
        Object obj = getInternal(keys);
        return Objects.nonNull(obj) ? obj:defaultValue;
    }

    public static Object get(String path, String keys) {
        return getInstance().getInternal(path, keys);
    }

    public static Object get(String keys){
        return getInstance().getInternal(keys);
    }

    public static String getString(String path, String keys){
        return getInstance().getStringInternal(path, keys);
    }

    public static String getString(String keys){
        return getInstance().getStringInternal(keys);
    }

    public static Object getOrDefault(String path, String keys, Object defaultValue){
        return getInstance().getOrDefaultInternal(path, keys, defaultValue);
    }

    public static Object getOrDefault(String keys, Object defaultValue){
        return getInstance().getOrDefaultInternal(keys, defaultValue);
    }

    public static List<Object> getAll(String path, String keys){
        return getInstance().getAllInternal(path, keys);
    }

    public static <T> T toEntity(String path, Constructor constructor){
        return (T) getInstance().toEntityInternal(path, constructor);
    }
}
