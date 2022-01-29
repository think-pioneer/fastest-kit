package xyz.thinktest.fastestapi.core.enhance.joinpoint;

/**
 * @Date: 2021/10/29
 */
public class Target {
    private Object instance;

    public Object getInstance() {
        return instance;
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
