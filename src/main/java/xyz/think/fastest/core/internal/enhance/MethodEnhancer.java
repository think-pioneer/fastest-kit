package xyz.think.fastest.core.internal.enhance;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Date: 2021/11/3
 */
interface MethodEnhancer extends MethodInterceptor {

    /**
     *
     * @param origin 被代理方法的对象
     * @param method 被代理的方法
     * @param args 被代理方法的参数
     * @param methodProxy 代理引用
     * @return
     * @throws Throwable
     */
    Object intercept(Object origin, Method method, Object[] args, MethodProxy methodProxy) throws Throwable;
}
