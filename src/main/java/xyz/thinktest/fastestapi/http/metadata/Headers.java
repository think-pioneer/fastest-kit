package xyz.thinktest.fastestapi.http.metadata;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        if(Objects.nonNull(header)) {
            super.add(header);
        }
        return this;
    }

    public Headers write(int index, Header header){
        if(Objects.nonNull(header) && index > 0 && index < this.size()) {
            super.add(index, header);
        }
        return this;
    }

    public Headers writeAll(Headers headers){
        if(CollectionUtils.isNotEmpty(headers)) {
            super.addAll(headers.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
        return this;
    }

    public Headers writeAll(Header... headers){
        if(null != headers && headers.length > 0){
            super.addAll(Arrays.stream(headers).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        return this;
    }

    public Headers writeAll(int index, Headers headers){
        if(CollectionUtils.isNotEmpty(headers) && index > 0 && index <= this.size()) {
            super.addAll(index, headers.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
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
        return super.stream().filter(Objects::nonNull).map((val) -> String.valueOf(val.getValue())).collect(Collectors.toList());
    }

    public void erasure(){
        super.clear();
    }
}
