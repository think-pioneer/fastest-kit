package xyz.thinktest.fastest.common.exceptions;

public class DateException extends FastestBasicException{
    public DateException(Throwable cause){
        super(cause);
    }

    public DateException(String msg){
        super(msg);
    }

    public DateException(String msg, Throwable cause){
        super(msg, cause);
    }

    public DateException(){
        super();
    }
}
