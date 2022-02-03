package xyz.thinktest.fastestapi.http.metadata;

import java.util.HashMap;

/**
 * @Date: 2020/10/16
 */
public class MetaMap extends HashMap<Object, Meta>{
    MetaMap(){}

    public static MetaMap newEmpty(){
        return new MetaMap();
    }
}
