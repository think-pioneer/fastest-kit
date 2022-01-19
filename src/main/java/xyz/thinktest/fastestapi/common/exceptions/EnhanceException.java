package xyz.thinktest.fastestapi.common.exceptions;

/**
 * @Date: 2021/10/29
 */
public class EnhanceException extends FastestBasicException {
    public EnhanceException(String msg){
        super(msg);
    }

    public EnhanceException(Throwable cause){
        super(cause);
    }

    public EnhanceException(String msg, Throwable cause){
        super(msg, cause);
    }
}
