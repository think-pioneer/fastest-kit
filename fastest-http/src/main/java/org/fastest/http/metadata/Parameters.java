package org.fastest.http.metadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date: 2020/10/16
 */
public class Parameters extends MetaMap {

    public Parameters write(Object key, Object value){
        this.put(key, new Parameter(key, value));
        return this;
    }

    public Parameters writeAll(Parameters parameters){
        this.putAll(parameters);
        return this;
    }

    public Meta readParameter(String key){
        return this.get(key);
    }

    public Object readParameterValue(String key){
        return this.readParameter(key).getValue();
    }

    public MetaList readAllParameter(){
        MetaList list = new MetaList();
        list.addAll(this.values());
        return list;
    }

    public List<Object> readAllParameterValue(){
        return this.values().stream().map(Meta::getValue).collect(Collectors.toList());
    }

    public void erasure(){
        this.clear();
    }
}
