package xyz.think.fastest.common.exceptions;

/**
 * @Date: 2020/10/16
 */
public class FastestBasicException extends RuntimeException{
    public FastestBasicException(){
        super();
    }

    public FastestBasicException(String message){
        super(message);
    }

    public FastestBasicException(String message, Throwable cause){
        super(message, cause);
    }

    public FastestBasicException(Throwable cause){
        super(cause);
    }
}
