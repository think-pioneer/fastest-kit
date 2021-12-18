package org.fastest.core.internal;

import org.fastest.common.exceptions.FastestBasicException;
import org.fastest.common.exceptions.ReflectionException;
import org.fastest.utils.ObjectUtil;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Objects;

/**
 * @Date: 2021/12/5
 */
public class ReflectTool {
    public static Object get(Object object, String name){
        try{
            InvocationHandler handler = Proxy.getInvocationHandler(object);
            Field fieldHandler = handler.getClass().getDeclaredField("memberValues");
            fieldHandler.setAccessible(true);
            Map<?, ?> o = (Map<?, ?>) fieldHandler.get(handler);
            return o.get(name);
        } catch (Exception e){
            throw new ReflectionException(ObjectUtil.format("get filed value error:{}", e.getMessage()), e.getCause());
        }
    }

    /**
     * Gets the true type of the collection element
     */
    public static Class<?> getCollectionGenericRealType(Type type){
        try {
            if(Objects.nonNull(type) && type instanceof ParameterizedType){
                ParameterizedType pt = (ParameterizedType) type;
                // 得到泛型里的class类型对象
                return (Class<?>)pt.getActualTypeArguments()[0];
            }
            throw new FastestBasicException(ObjectUtil.format("get Collection element real type error: type is null or ont inherit ParameterizedType"));
        }catch (Exception e){
            throw new FastestBasicException(ObjectUtil.format("get Collection element real type error:{}", e.getMessage()), e.getCause());
        }
    }
}
