package org.fastest.core.cnhance.constructor;

/**
 * @Date: 2021/11/7
 */
public class ConstructorProperty {
    private final Class<?> type;
    private final Object object;

    public ConstructorProperty(Class<?> type, Object object){
        this.type = type;
        this.object = object;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
