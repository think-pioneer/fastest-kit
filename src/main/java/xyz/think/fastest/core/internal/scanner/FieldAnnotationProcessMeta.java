package xyz.think.fastest.core.internal.scanner;

import xyz.think.fastest.core.internal.enhance.AnnotationGardener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段注解的元数据
 * @author: aruba
 * @date: 2022-01-27
 */
public class FieldAnnotationProcessMeta implements Serializable {
    private static final long serialVersionUID = -5234607711819228521L;
    /**
     * 处理字段注解时的前置注解
     */
    private final List<AnnotationGardener> beforeAnnotations;
    private Field field;

    public FieldAnnotationProcessMeta(){
        this.beforeAnnotations = new ArrayList<>();
    }

    public List<AnnotationGardener> getBeforeAnnotations() {
        return beforeAnnotations;
    }

    public void setBeforeAnnotations(List<AnnotationGardener> annotationGardeners){
        this.beforeAnnotations.addAll(annotationGardeners);
    }

    public void setBeforeAnnotation(AnnotationGardener annotationGardener){
        this.beforeAnnotations.add(annotationGardener);
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "FieldAnnotationProcessMeta{" +
                "beforeAnnotations=" + beforeAnnotations +
                ", field=" + field +
                '}';
    }
}
