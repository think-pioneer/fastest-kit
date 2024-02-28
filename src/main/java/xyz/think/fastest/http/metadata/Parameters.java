package xyz.think.fastest.http.metadata;

import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Date: 2020/10/16
 */
public class Parameters extends MetaMap {

    private static final long serialVersionUID = 4426487049324045664L;

    private Parameters(){}

    public static Parameters newEmpty(){
        return new Parameters();
    }

    public Parameters write(Object key, Object value){
        Object tmp = this.get(key);
        List<Object> values;
        if(tmp != null){
            values = (List<Object>) ((Meta)tmp).getValue();
        }else{
            values = new ArrayList<>();
        }
        values.add(value);
        this.put(key, new Parameter(key, values));
        return this;
    }

    public Parameters writeAll(Parameters parameters){
        if(MapUtils.isNotEmpty(parameters)){
            this.putAll(parameters);
        }
        return this;
    }

    public Parameters writeAll(Parameter... parameters){
        if(null != parameters && parameters.length > 0){
            for(Parameter parameter:parameters){
                if(Objects.nonNull(parameter))
                    this.put(parameter.getKey(), parameter);
            }
        }

        return this;
    }

    public Meta readParameter(String key){
        return this.get(key);
    }

    public Object readParameterValue(String key){
        return this.readParameter(key).getValue();
    }

    public MetaList readAllParameter(){
        MetaList list = MetaList.newEmpty();
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
