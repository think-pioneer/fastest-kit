package xyz.thinktest.fastest.http.metadata;

/**
 * @Date: 2020/10/16
 */
public class Form extends Meta{
    private final String key;
    private final String value;

    public Form(Object key, Object value){
        this.key = String.valueOf(key);
        this.value = String.valueOf(value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Form{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
