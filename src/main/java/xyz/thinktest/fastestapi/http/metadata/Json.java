package xyz.thinktest.fastestapi.http.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import xyz.thinktest.fastestapi.common.json.JSONFactory;

import java.util.Objects;

/**
 * @Date: 2020/10/16
 */
public class Json extends Meta {
    private JsonNode jsonNode;

    private Json(){}

    public static Json newEmptyInstance(){
        return new Json();
    }

    public void append(ObjectNode json){
        this.jsonNode = json;
    }

    public void append(ArrayNode json){
        this.jsonNode = json;
    }

    public void append(String jsonString){
        this.jsonNode = JSONFactory.stringToJson(jsonString);
    }

    public void append(Object entity){
        this.jsonNode = JSONFactory.objectToJson(entity);
    }

    public void append(JsonNode jsonNode){
        this.jsonNode = jsonNode;
    }

    public void recovery(){
        this.jsonNode = JSONFactory.createObjectNode();
    }

    public JsonNode getJsonNode(){
        if(Objects.isNull(this.jsonNode)){
            return JSONFactory.createObjectNode();
        }
        return this.jsonNode;
    }

    @Override
    public String toString(){
        return Objects.isNull(this.jsonNode) ? "" : this.jsonNode.toString();
    }

    @Override
    public Object getKey() {
        return jsonNode;
    }

    @Override
    public Object getValue() {
        return jsonNode;
    }
}
