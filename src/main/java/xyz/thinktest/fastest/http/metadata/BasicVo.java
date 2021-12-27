package xyz.thinktest.fastest.http.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import xyz.thinktest.fastest.common.json.JSONFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * @Date: 2021/10/30
 */
public class BasicVo<T> implements Serializable {

    private static final long serialVersionUID = -5886543239057370030L;

    public Map<?, ?> toMap(){
        return JSONFactory.stringToObject(JSONFactory.objectToString(this), new TypeReference<Map<?, ?>>() {});
    }
}
