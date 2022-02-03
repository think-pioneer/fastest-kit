package xyz.thinktest.fastestapi.http.metadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date: 2020/10/16
 */
public class Headers extends MetaList {

    private static final long serialVersionUID = 7863193005200041573L;

    private Headers(){}

    public static Headers newEmpty(){
        return new Headers();
    }

    public Headers write(Header header){
        super.add(header);
        return this;
    }

    public Headers write(int index, Header header){
        super.add(index, header);
        return this;
    }

    public Headers writeAll(Headers headers){
        super.addAll(headers);
        return this;
    }

    public Headers writeAll(int index, Headers headers){
        super.addAll(index, headers);
        return this;
    }

    public Meta readHeader(String key){
        return super.stream().filter((val) -> val.getKey().equals(key)).findAny().orElse(null);
    }

    public String readHeaderValue(String key){
        return String.valueOf(this.readHeader(key).getValue());
    }

    public MetaList readAllHeader(){
        return this;
    }

    public List<String> readAllHeaderValue(){
        return super.stream().map((val) -> String.valueOf(val.getValue())).collect(Collectors.toList());
    }

    public void erasure(){
        super.clear();
    }
}
