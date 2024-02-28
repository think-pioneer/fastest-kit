package xyz.think.fastest.common.exceptions;

/**
 * @Date: 2021/12/5
 */
public class ReflectionException extends FastestBasicException {
    public ReflectionException(){
        super();
    }

    public ReflectionException(String message){
        super(message);
    }

    public ReflectionException(Throwable cause){
        super(cause);
    }

    public ReflectionException(String message, Throwable cause){
        super(message, cause);
    }
}
