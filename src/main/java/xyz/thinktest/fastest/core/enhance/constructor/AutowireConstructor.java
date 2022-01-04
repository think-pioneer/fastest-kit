package xyz.thinktest.fastest.core.enhance.constructor;

/**
 * @Date: 2021/11/7
 */
public class AutowireConstructor {
    private final Class<?> type;
    private final Object object;

    public AutowireConstructor(Class<?> type, Object object){
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
