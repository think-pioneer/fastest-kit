package org.fastest.core.internal.enhance.methodhelper;

import org.fastest.utils.reflects.MethodHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * @Date: 2021/12/5
 */
class HockMethodTool {

    public static void invoke(Class<?> targetClass, String targetMethod, Object[] actualArgs, int[] argsIndex) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] realArgsType = new Class[argsIndex.length];
        Object[] realArgs = new Object[argsIndex.length];
        for(int i = 0;i < argsIndex.length;i++){
            Object actualArg = actualArgs[argsIndex[i]];
            realArgs[i] = actualArg;
            realArgsType[i] = actualArg.getClass();
        }
        MethodHelper.getInstance(targetClass.getDeclaredConstructor().newInstance(), targetMethod, realArgsType).invoke(realArgs);
    }
}
