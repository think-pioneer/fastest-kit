package xyz.think.fastest.core.enhance.joinpoint.method;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录被代理方法的返回值
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
