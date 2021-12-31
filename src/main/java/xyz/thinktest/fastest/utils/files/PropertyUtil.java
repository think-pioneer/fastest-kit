package xyz.thinktest.fastest.utils.files;

import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastest.common.exceptions.FileException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2020/10/24
 */
public enum  PropertyUtil {
    INSTANCE;
    private final Properties properties;
    private final ConcurrentHashMap<String, Properties> propertiesMap;
    PropertyUtil(){
        this.properties = new Properties();
        this.propertiesMap = new ConcurrentHashMap<>();
        this.properties.putAll(System.getProperties());
        List<File> fileList = new ArrayList<>();
        FileUtil.collect(FileUtil.getResourcesPath(), fileList, new String[]{"properties"});
        try {
            for (File file : fileList) {
                String name = file.getAbsolutePath();
                if (!name.endsWith("log4j2.properties") && !name.contains("log4j") && !name.endsWith("pom.properties")) {
                    Properties properties = new Properties();
                    properties.load(FileUtil.read(file.getAbsolutePath()));
                    this.properties.putAll(properties);
                    propertiesMap.put(name, properties);
                }
            }
        }catch (IOException e){
            throw new FileException("properties initialize error", e);
        }
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * {@code null} if the property is not found.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     */
    private String getPropertyInternal(String key){
        return this.properties.getProperty(key);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * {@code null} if the property is not found.
     *
     * @param   key   the property key.
     * @param   path  properties file path
     * @return  the value in this property list with the specified key value.
     */
    private String getPropertyInternal(String key, String path){
        path = path.replace("/", File.separator).replace("\\", File.separator);
        if(StringUtils.isEmpty(path)){
            return getPropertyInternal(key);
        }
        File file = new File(path);
        if(!file.isAbsolute()){
            path = new File(FileUtil.getResourcesPath(), path).getAbsolutePath();
            for(Map.Entry<String, Properties> entry:propertiesMap.entrySet()){
                String fileName = entry.getKey();
                if(fileName.equals(path)){
                    return entry.getValue().getProperty(key);
                }
            }
        }
        return null;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key.equals(k))},
     * then this method returns {@code v}; otherwise it returns
     * {@code null}.  (There can be at most one such mapping.)
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws NullPointerException if the specified key is null
     */
    private Object getInternal(String key){
        return this.properties.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key.equals(k))},
     * then this method returns {@code v}; otherwise it returns
     * {@code null}.  (There can be at most one such mapping.)
     *
     * @param key the key whose associated value is to be returned
     * @param path properties file path
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws NullPointerException if the specified key is null
     */
    private Object getInternal(String key, String path){
        path = path.replace("/", File.separator).replace("\\", File.separator);
        if(StringUtils.isEmpty(path)){
            return getInternal(key);
        }
        File file = new File(path);
        if(!file.isAbsolute()){
            path = new File(FileUtil.getResourcesPath(), path).getAbsolutePath();
            for(Map.Entry<String, Properties> entry:propertiesMap.entrySet()){
                String fileName = entry.getKey();
                if(fileName.equals(path)){
                    return entry.getValue().get(key);
                }
            }
        }
        return null;
    }

    /**
     * get properties file's Properties object
     * @param path properties file path
     * @return Properties object
     */
    private Properties getPropertiesInternal(String path){
        path = path.replace("/", File.separator).replace("\\", File.separator);
        if(StringUtils.isEmpty(path)){
            return new Properties();
        }
        File file = new File(path);
        if(!file.isAbsolute()){
            path = new File(FileUtil.getResourcesPath(), path).getAbsolutePath();
            for(Map.Entry<String, Properties> entry:propertiesMap.entrySet()){
                String fileName = entry.getKey();
                if(fileName.equals(path)){
                    return entry.getValue();
                }
            }
        }
        return new Properties();
    }

    /**
    * Returns the value to which the specified key is mapped, or
    * {@code defaultValue} if this map contains no mapping for the key.
    *
    * @implSpec
    * The default implementation makes no guarantees about synchronization
    * or atomicity properties of this method. Any implementation providing
    * atomicity guarantees must override this method and document its
    * concurrency properties.
    *
    * @param key the key whose associated value is to be returned
    * @param defaultValue the default mapping of the key
    * @return the value to which the specified key is mapped
    * */
    private Object getOrDefaultInternal(String key, Object defaultValue){
        return this.properties.getOrDefault(key, defaultValue);
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @implSpec
     * The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @param path properties file path
     * @return the value to which the specified key is mapped
     * */
    private Object getOrDefaultInternal(String key, Object defaultValue, String path){
        path = path.replace("/", File.separator).replace("\\", File.separator);
        if(StringUtils.isEmpty(path)){
            return getOrDefaultInternal(key, defaultValue);
        }
        File file = new File(path);
        if(!file.isAbsolute()){
            path = new File(FileUtil.getResourcesPath(), path).getAbsolutePath();
            for(Map.Entry<String, Properties> entry:propertiesMap.entrySet()){
                String fileName = entry.getKey();
                if(fileName.equals(path)){
                    return entry.getValue().getOrDefault(key, defaultValue);
                }
            }
        }
        return null;

    }

    public static String getProperty(String key){
        return PropertyUtil.INSTANCE.getPropertyInternal(key);
    }

    public static String getProperty(String key, String path){
        return PropertyUtil.INSTANCE.getPropertyInternal(key, path);
    }

    public static Object get(String key){
        return PropertyUtil.INSTANCE.getInternal(key);
    }

    public static Object get(String key, String path){
        return PropertyUtil.INSTANCE.getInternal(key, path);
    }

    public static Properties getProperties(String path){
        return PropertyUtil.INSTANCE.getPropertiesInternal(path);
    }

    public static Object getOrDefault(String key, Object defaultValue){
        return PropertyUtil.INSTANCE.getOrDefaultInternal(key, defaultValue);
    }

    public static Object getOrDefault(String key, Object defaultValue, String path){
        return PropertyUtil.INSTANCE.getOrDefaultInternal(key, defaultValue,path);
    }
}
