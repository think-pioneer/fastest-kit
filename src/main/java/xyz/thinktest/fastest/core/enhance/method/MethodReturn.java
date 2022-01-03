package xyz.thinktest.fastest.core.enhance.method;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2021/12/27
 */
public class MethodReturn {
    private final List<Object> returnValues = new ArrayList<>();

    public void setReturnValue(List<Object> returnValues) {
        this.returnValues.addAll(returnValues);
    }

    public List<Object> getReturnValue() {
        return returnValues;
    }

    @Override
    public String toString() {
        return "MethodReturn{" +
                "returnValues=" + returnValues +
                '}';
    }
}
