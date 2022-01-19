package xyz.thinktest.fastestapi.http.metadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date: 2020/10/16
 */
public class Forms extends MetaMap {

    private static final long serialVersionUID = 3728255718906321593L;

    private Forms(){}

    public static Forms newEmptyInstance(){
        return new Forms();
    }

    /**
     * write form key-value to Forms
     * @param key form key
     * @param value form value
     */
    public Forms write(Object key, Object value){
        super.put(key, new Form(key, value));
        return this;
    }

    /**
     * write existing forms object
     */
    public Forms writeAll(Forms forms){
        this.putAll(forms);
        return this;
    }

    /**
     * Get an element of the form
     */
    public Meta readForm(Object key){
        return this.get(key);
    }

    /**
     * Get the value of an element of the form
     */
    public Object readFormValue(String key){
        return this.readForm(key).getValue();
    }

    /**
     * Get all the elements of the form
     */
    public MetaList readAllForm(){
        MetaList list = MetaList.newEmptyInstance();
        list.addAll(this.values());
        return list;
    }

    /**
     * Get the values of all elements of the form
     */
    public List<Object> readAllFormValue(){
        return this.values().stream().map(Meta::getValue).collect(Collectors.toList());
    }

    public void erasure(){
        this.clear();
    }
}
