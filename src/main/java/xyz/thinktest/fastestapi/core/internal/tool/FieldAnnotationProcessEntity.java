package xyz.thinktest.fastestapi.core.internal.tool;

import xyz.thinktest.fastestapi.core.internal.enhance.AnnotationGardener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: aruba
 * @date: 2022-01-27
 */
public class FieldAnnotationProcessEntity implements Serializable {
    private static final long serialVersionUID = -5234607711819228521L;
    private final List<AnnotationGardener> beforeAnnotations;
    private Field field;

    public FieldAnnotationProcessEntity(){
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
        return "FieldAnnotationProcessEntity{" +
                "beforeAnnotations=" + beforeAnnotations +
                ", field=" + field +
                '}';
    }
}
