package xyz.thinktest.fastestapi.common.exceptions;

/**
 * @Date: 2021/11/7
 */
public class ValueException extends FastestBasicException {
    public ValueException(){
        super();
    }

    public ValueException(String msg){
        super(msg);
    }

    public ValueException(Throwable cause){
        super(cause);
    }

    public ValueException(String msg, Throwable cause){
        super(msg, cause);
    }
}
