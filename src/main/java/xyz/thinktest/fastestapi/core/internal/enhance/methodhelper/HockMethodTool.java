package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.utils.reflects.MethodHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * @Date: 2021/12/5
 */
class HockMethodTool {

    public static <T> T invoke(Class<?> targetClass, String targetMethod, Object[] actualArgs, int[] argsIndex) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        int actualArgsLen = actualArgs.length;
        int[] _argsIndex;
        if(argsIndex.length == 0){
            _argsIndex = new int[actualArgsLen];
            for(int i = 0;i < actualArgsLen; i++){
                _argsIndex[i] = i+1;
            }
        }else{
            _argsIndex = argsIndex;
        }
        int argsIndexLen = _argsIndex.length;
        Class<?>[] realArgsType = new Class[argsIndexLen];
        Object[] realArgs = new Object[argsIndexLen];
        for (int i = 0; i < argsIndexLen; i++) {
            Object actualArg = actualArgs[_argsIndex[i] - 1];//argsIndex中标识的下表的起始为1，所以需要-1才是真实小标
            realArgs[i] = actualArg;
            realArgsType[i] = actualArg.getClass();
        }
        MethodHelper<T> methodHelper = MethodHelper.getInstance(targetClass.getDeclaredConstructor().newInstance(), targetMethod, realArgsType);
        return methodHelper.invoke(realArgs);
    }
}
