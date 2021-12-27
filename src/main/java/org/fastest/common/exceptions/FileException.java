package org.fastest.common.exceptions;

/**
 * @Date: 2020/10/16
 */
public class FileException extends FastestBasicException {
    public FileException(){
        super();
    }

    public FileException(String message){
        super(message);
    }

    public FileException(Throwable cause){
        super(cause);
    }

    public FileException(String message, Throwable cause){
        super(message, cause);
    }
}
