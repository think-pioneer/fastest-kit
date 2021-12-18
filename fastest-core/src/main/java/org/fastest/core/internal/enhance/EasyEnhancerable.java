package org.fastest.core.internal.enhance;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Date: 2021/11/3
 */
interface EasyEnhancerable extends MethodInterceptor {
    Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable;
}
