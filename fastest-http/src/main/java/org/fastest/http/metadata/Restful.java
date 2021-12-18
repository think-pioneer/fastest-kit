package org.fastest.http.metadata;

import lombok.Getter;
import lombok.ToString;

/**
 * @Date: 2020/10/17
 */
@Getter
@ToString
public class Restful extends Meta {
    private final String key;
    private final String value;

    public Restful(Object key, Object value){
        this.key = String.valueOf(key);
        this.value = String.valueOf(value);
    }
}
