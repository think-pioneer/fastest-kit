package xyz.thinktest.fastestapi.http.metadata;

import xyz.thinktest.fastestapi.common.exceptions.EnhanceException;
import xyz.thinktest.fastestapi.utils.ObjectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Date: 2020/4/4
 */

public class Restfuls extends MetaMap {

    private static final long serialVersionUID = -2859922274054222800L;

    private Restfuls(){}

    public static Restfuls newEmpty(){
        return new Restfuls();
    }

    public Restfuls write(String key, String value) {
        this.put(key, new Restful(key, value));
        return this;
    }

    public Restfuls write(String key, Restful value) {
        this.put(key, value);
        return this;
    }

    public Restfuls writeAll(Restfuls restfuls){
        this.putAll(restfuls);
        return this;
    }

    public Meta readRestful(String key) {
        return this.get(key);
    }

    public Object readRestfulValue(String key) {
        return this.readRestful(key).getValue();
    }

    public MetaList readAllRestful(){
        MetaList list = MetaList.newEmpty();
        list.addAll(this.values());
        return list;
    }

    public List<Object> readAllRestfulValue(){
        return this.values().stream().map(Meta::getValue).collect(Collectors.toList());
    }

    public void erasure(){
        this.clear();
    }

    public String buildUrl(String url){
        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(url);
        Map<String, String> restParams = new HashMap<>();
        while (matcher.find()){
            restParams.put(matcher.group(1), matcher.group());
        }
        if(restParams.isEmpty()){
            throw new EnhanceException(ObjectUtil.format("url:[{}] not restful url", url));
        }
        AtomicReference<String> newUrl = new AtomicReference<>();
        this.forEach((key, value) -> newUrl.set(url.replace(restParams.get(key), String.valueOf(value.getValue()))));
        return newUrl.get();
    }
}