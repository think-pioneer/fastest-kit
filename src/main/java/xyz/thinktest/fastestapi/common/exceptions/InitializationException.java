package xyz.thinktest.fastestapi.common.exceptions;

/**
 * @author: aruba
 * @date: 2022-01-27
 */
public class InitializationException extends FastestBasicException{
    public InitializationException(){
        super();
    }

    public InitializationException(String message){
        super(message);
    }

    public InitializationException(Throwable cause){
        super(cause);
    }

    public InitializationException(String message, Throwable cause){
        super(message, cause);
    }
}
