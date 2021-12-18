package org.fastest.http.metadata;

import lombok.Getter;
import lombok.ToString;

/**
 * @Date: 2020/10/16
 */
@Getter
@ToString
public class Header extends Meta {
    private final String key;
    private final String value;

    public Header(String key, String value){
        this.key = key;
        this.value = value;
    }
}
