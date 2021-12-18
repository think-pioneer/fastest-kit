package org.fastest.http.metadata;

import lombok.Data;
import lombok.ToString;
import org.fastest.common.exceptions.FastestBasicException;

/**
 * @Date: 2020/10/16
 */
@Data
@ToString
public abstract class Meta {
    private Object key;
    private Object value;

    public Object getKey(){
        throw new FastestBasicException("method getKey must override by subclass");
    }

    public Object getValue(){
        throw new FastestBasicException("method getValue must override by subclass");
    }
}
