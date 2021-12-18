package org.fastest.http.metadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date: 2020/4/4
 */

public class Restfuls extends MetaMap {

    public Restfuls write(String key, String value) {
        this.put(key, new Restful(key, value));
        return this;
    }

    public Meta readRestful(String key) {
        return this.get(key);
    }

    public Object readRestfulValue(String key) {
        return this.readRestful(key).getValue();
    }

    public MetaList readAllRestful(){
        MetaList list = new MetaList();
        list.addAll(this.values());
        return list;
    }

    public List<Object> readAllRestfulValue(){
        return this.values().stream().map(Meta::getValue).collect(Collectors.toList());
    }

    public void erasure(){
        this.clear();
    }
}