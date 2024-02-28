package xyz.think.fastest.core.enhance.constructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2021/11/7
 */
public class ConstructorProperty {
    private final List<Class<?>> types;
    private final List<Object> objects;

    public ConstructorProperty(List<Class<?>> types, List<Object> objects){
        this();
        this.types.addAll(types == null ? new ArrayList<>() : types);
        this.objects.addAll(objects == null ? new ArrayList<>() : objects);
    }

    public ConstructorProperty() {
        this.types = new ArrayList<>();
        this.objects = new ArrayList<>();
    }

    public List<Class<?>> getTypes() {
        return types;
    }

    public List<Object> getObjects() {
        return objects;
    }
}
