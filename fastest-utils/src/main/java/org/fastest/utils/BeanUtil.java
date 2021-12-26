package org.fastest.utils;

/**
 * @Date: 2021/12/5
 */
import org.fastest.common.exceptions.FastestBasicException;

import java.io.*;

public class BeanUtil {
    public static <T> T deepCopy(T src){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(src);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            T deepObj = (T) ois.readObject();
            ois.close();
            bais.close();
            oos.close();
            baos.close();
            return deepObj;
        }catch (IOException | ClassNotFoundException e){
            throw new FastestBasicException("deep copy object fail", e);
        }
    }
}
