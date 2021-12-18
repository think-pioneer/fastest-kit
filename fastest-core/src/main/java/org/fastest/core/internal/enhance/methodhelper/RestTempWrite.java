package org.fastest.core.internal.enhance.methodhelper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.fastest.common.json.JSONFactory;

import java.util.Objects;

/**
 * @Date: 2021/10/29
 */
public final class RestTempWrite {
    private static final ArrayNode arrayNode = JSONFactory.createArrayNode();

    private RestTempWrite(){}

    public static void add(ObjectNode apiNode){
        if(Objects.nonNull(apiNode) && !apiNode.isEmpty()){
            arrayNode.add(apiNode);
        }
    }

    public static ArrayNode getAllApi(){
        return arrayNode;
    }

    public static String pretty(){
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        if(Objects.nonNull(arrayNode) && arrayNode.size() > 0){
            arrayNode.forEach(node -> sb.append("\t").append(node.toString()).append(",").append("\n"));
            sb.deleteCharAt(sb.length() - 2);

        }
        sb.append("]");
        return sb.toString();
    }
}
