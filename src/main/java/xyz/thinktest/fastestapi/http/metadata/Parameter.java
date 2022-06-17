package xyz.thinktest.fastestapi.http.metadata;

/**
 * @Date: 2020/10/16
 */
public class Parameter extends Meta {
    private final String key;
    private final Object value;

    public Parameter(Object key, Object value){
        this.key = String.valueOf(key);
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
