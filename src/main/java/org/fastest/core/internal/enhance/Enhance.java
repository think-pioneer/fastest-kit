package org.fastest.core.internal.enhance;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

/**
 * @Date: 2020/10/28
 */
class Enhance extends Enhancer {

    public void setClass(Class<?> clazz){
        super.setSuperclass(clazz);
    }

    public void setHandler(Callback callback){
        super.setCallback(callback);
    }
}
