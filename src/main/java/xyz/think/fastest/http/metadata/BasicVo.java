package xyz.think.fastest.http.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import xyz.think.fastest.common.json.JSONFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * @Date: 2021/10/30
 */
public class BasicVo implements Serializable {

    private static final long serialVersionUID = -5886543239057370030L;
    protected BasicVo(){}

    public Map<?, ?> toMap(){
        return JSONFactory.stringToObject(JSONFactory.objectToString(this), new TypeReference<Map<?, ?>>() {});
    }
}
