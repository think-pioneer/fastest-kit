package xyz.think.fastest.http.metadata;

import java.util.Hashtable;

/**
 * @Date: 2020/10/16
 */
public class MetaMap extends Hashtable<Object, Meta> {
    private static final long serialVersionUID = 1037095373651036654L;

    MetaMap(){}

    public void copy(MetaMap map){
        this.clear();
        this.putAll(map);
    }

    public static MetaMap newEmpty(){
        return new MetaMap();
    }
}
