package xyz.thinktest.fastest.utils.files.iostream;

import xyz.thinktest.fastest.common.exceptions.FastestBasicException;
import xyz.thinktest.fastest.common.exceptions.FileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Date: 2021/12/9
 */
public final class InputStreamOptional<T> {
    private final T value;
    private ByteArrayOutputStream baos;
    private static final InputStreamOptional<?> EMPTY = new InputStreamOptional<>();

    public InputStreamOptional(){
        this.value = null;
    }

    public static <T> InputStreamOptional<T> empty(){
        @SuppressWarnings("unchecked")
        InputStreamOptional<T> t = (InputStreamOptional<T>) EMPTY;
        return t;
    }

    public static <T> InputStreamOptional<T> of(T value){
        return new InputStreamOptional<>(value);
    }

    public static <T> InputStreamOptional<T> ofNullable(T value){
        return value == null ? empty() : of(value);
    }

    public InputStreamOptional(T value){
        this.value = value;
    }

    public InputStreamOptional<T> ifPresent(InputStreamPersistence<T> action){
        if(value != null){
            InputStreamPersistence<T> persistence = this::toByteArrayOutputStream;
            action.from(persistence).to(value);
            return this;
        }
        throw new FastestBasicException("InputStream is null");
    }

    public InputStreamOptional<T> ifPresent(){
        if(value != null){
            InputStreamPersistence<T> persistence = this::toByteArrayOutputStream;
            InputStreamPersistence<T> action = ((iso) -> {});
            action.from(persistence).to(value);
            return this;
        }
        throw new FastestBasicException("InputStream is null");
    }

    public ByteArrayOutputStream get(){
        if(this.baos == null){
            throw new FastestBasicException("ByteArrayOutputStream is null");
        }
        return this.baos;
    }

    private void toByteArrayOutputStream(T inputStream) {
        try {
            InputStream is = (InputStream) inputStream;
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        }catch (IOException e){
            throw new FileException("IOException", e);
        }
    }
}
