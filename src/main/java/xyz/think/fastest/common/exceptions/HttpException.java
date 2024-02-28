package xyz.think.fastest.common.exceptions;

/**
 * @Date: 2020/10/16
 */
public class HttpException extends FastestBasicException {
    public HttpException(){
        super();
    }

    public HttpException(String message){
        super(message);
    }

    public HttpException(Throwable cause){
        super(cause);
    }

    public HttpException(String message, Throwable cause){
        super(message, cause);
    }
}
