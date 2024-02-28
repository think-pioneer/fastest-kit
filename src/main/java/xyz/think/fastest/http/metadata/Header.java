package xyz.think.fastest.http.metadata;

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
    public boolean equals(Object header){
        if(!(header instanceof Header)){
            return false;
        }
        Header tmp = (Header) header;
        if(this.key != null && this.value != null){
            return this.key.equals(tmp.key) && this.value.equals(tmp.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Header{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
