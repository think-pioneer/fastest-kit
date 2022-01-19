package xyz.thinktest.fastestapi.http.metadata;

/**
 * @Date: 2020/10/17
 */
public class Restful extends Meta {
    private final String key;
    private final String value;

    public Restful(Object key, Object value){
        this.key = String.valueOf(key);
        this.value = String.valueOf(value);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Restful{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
