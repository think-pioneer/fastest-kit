package xyz.think.fastest.core.enhance.joinpoint;

/**
 * @Date: 2021/10/29
 */
@SuppressWarnings("unchecked")
public class Target {
    private Object instance;

    public <T> T getInstance() {
        return (T) instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "Target{" +
                "instance=" + instance +
                '}';
    }
}
