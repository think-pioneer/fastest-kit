package xyz.thinktest.fastest.core;

import xyz.thinktest.fastest.core.internal.enhance.EnhanceFactory;

/**
 * @Date: 2021/12/7
 */
public enum ApplicationBean {
    ENHANCE{
        @Override
        public Object newInstance(Class<?> clazz, Class<?> callbackType) {
            return EnhanceFactory.enhance(clazz, callbackType);
        }

        @Override
        public Object newInstance(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments, Class<?> callbackType) {
            return EnhanceFactory.enhance(clazz, argumentTypes, arguments, callbackType);
        }
    },
    ORIGIN{
        @Override
        public Object newInstance(Class<?> clazz, Class<?> callbackType) {
            return EnhanceFactory.origin(clazz);
        }

        @Override
        public Object newInstance(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments, Class<?> callbackType) {
            return EnhanceFactory.origin(clazz, argumentTypes, arguments);
        }
    };

    public abstract Object newInstance(Class<?> clazz, Class<?> callbackType);
    public abstract Object newInstance(Class<?> clazz, Class<?>[] argumentTypes, Object[] arguments, Class<?> callbackType);
}
