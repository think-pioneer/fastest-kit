package xyz.think.fastest.common.exceptions;

/**
 * @Date: 2020/10/16
 */
public class JsonException extends FastestBasicException {

    public JsonException(){
        super();
    }

    public JsonException(String message){
        super(message);
    }

    public JsonException(Throwable cause){
        super(cause);
    }

    public JsonException(String message, Throwable cause){
        super(message, cause);
    }
}
