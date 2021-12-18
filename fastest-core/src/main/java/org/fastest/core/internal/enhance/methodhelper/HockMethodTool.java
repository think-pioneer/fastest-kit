package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.common.exceptions.EnhanceException;
import org.fastest.utils.ObjectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Date: 2021/12/5
 */
class HockMethodTool {

    public static void invoke(Class<?> targetClass, String targetMethod, Object[] actualArgs, int[] argsIndex){
        Class<?>[] realArgsType = new Class[argsIndex.length];
        Object[] realArgs = new Object[argsIndex.length];
        for(int i = 0;i < argsIndex.length;i++){
            Object actualArg = actualArgs[argsIndex[i]];
            realArgs[i] = actualArg;
            realArgsType[i] = actualArg.getClass();
        }
        Method method;
        try{
            method = targetClass.getMethod(targetMethod, realArgsType);
            if(Modifier.isStatic(method.getModifiers())){
                method.invoke(null, realArgs);
            } else {
                method.invoke(targetClass.getDeclaredConstructor().newInstance(), realArgs);
            }
        } catch (NoSuchMethodException e){
            throw new EnhanceException(ObjectUtil.format("not found method:{} in class:{}", targetMethod, targetClass.getName()), e);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e){
            throw new EnhanceException(ObjectUtil.format("exception executing method:{} in class{}", targetMethod, targetClass.getName()));
        }
    }
}
