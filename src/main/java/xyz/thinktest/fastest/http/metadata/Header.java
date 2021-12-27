package xyz.thinktest.fastest.http.metadata;

/**
 * @Date: 2020/10/16
 */
public class Header extends Meta {
    private final String key;
    private final String value;

    public Header(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Header{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
