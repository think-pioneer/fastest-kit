package xyz.thinktest.fastest.core.enhance.joinpoint;

/**
 * @Date: 2021/10/29
 */
public class Target<T> {
    private T instance;

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "Target{" +
                "instance=" + instance +
                '}';
    }
}
